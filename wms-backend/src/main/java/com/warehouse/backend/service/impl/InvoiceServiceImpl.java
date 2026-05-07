package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.InvoiceDetailRequest;
import com.warehouse.backend.dto.request.InvoiceRequest;
import com.warehouse.backend.dto.response.InvoiceResponse;
import com.warehouse.backend.entity.danhmuc.Customer;
import com.warehouse.backend.entity.danhmuc.Product;
import com.warehouse.backend.entity.hethong.User;
import com.warehouse.backend.entity.nghiepvu.Invoice;
import com.warehouse.backend.entity.nghiepvu.InvoiceDetail;
import com.warehouse.backend.mapper.InvoiceMapper;
import com.warehouse.backend.repository.*;
import com.warehouse.backend.service.IInvoiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements IInvoiceService {
    private final InvoiceMapper invoiceMapper;
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository  customerRepository;
    private final UserRepository userRepository;


    public InvoiceServiceImpl(InvoiceMapper invoiceMapper, InvoiceRepository invoiceRepository, IssueDetailRepository issueDetailRepository, ProductRepository productRepository, CustomerRepository customerRepository, UserRepository userRepository) {
        this.invoiceMapper = invoiceMapper;
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    // Hàm tìm hóa đơn
    public Invoice findInvoiceById(String invoiceId){
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã : " + invoiceId));
    }
    // Hàm tự đông tạo mã hóa đơn
    public String generateNextInvoiceId(){
        String maxId = invoiceRepository.findMaxInvoiceId();
        if (maxId == null) return "HD001";
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        return String.format("HD%03d", nextNumber);
    }
    // Hàm Laays hóa đơn
    @Override
    public List<InvoiceResponse> getAllInvoice(){
        return invoiceRepository.findAll()
                .stream().map(invoiceMapper::toInvoiceResponse).toList();
    }

    @Override
    public InvoiceResponse getInvoiceById(String invoiceId){
        Invoice invoice = findInvoiceById(invoiceId);
        return invoiceMapper.toInvoiceResponse(invoice);
    }
    @Override
    @Transactional
    public InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest){
        if(invoiceRequest.getInvoiceDetails() == null || invoiceRequest.getInvoiceDetails().isEmpty()){
            throw new RuntimeException("Hóa đơn phải có ít nhất một hàng");
        }
        // map dto sang entity
        Invoice invoice = invoiceMapper.toInvoiceEnity(invoiceRequest);
        //Tự động tạo mã
        invoice.setInvoiceId(generateNextInvoiceId());
        invoice.setInvoiceDate(LocalDate.now());

        // lấy id user
        User user = userRepository.findById(invoiceRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id : " + invoiceRequest.getUserId()));
        invoice.setUser(user);

        // lấy khách hàng
        Customer customer = customerRepository.findById(invoiceRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với id : " + invoiceRequest.getCustomerId()));
        invoice.setCustomer(customer);

        // Xử lý chi tiết hóa đơn
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(InvoiceDetailRequest invoiceDetailRequest : invoiceRequest.getInvoiceDetails()) {
            Product product = productRepository.findById(invoiceDetailRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Hàng hóa không tồn tại!"));
            // tạo chi tiết hóa đơn
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setInvoice(invoice);
            invoiceDetail.setProduct(product);
            invoiceDetail.setQuantity(invoiceDetailRequest.getQuantity());
            invoiceDetail.setPrice(invoiceDetailRequest.getPrice());

            // tính tổng tiền
            BigDecimal subTotal  = invoiceDetail.getSubTotal();
            // Cộng dồn vào Tổng tiền của tờ phiếu
            totalAmount = totalAmount.add(subTotal);
            // bỏ dòng này vào giỏ
            invoiceDetails.add(invoiceDetail);
        }
        invoice.setTotalAmount(totalAmount);
        invoice.setInvoiceDetails(invoiceDetails);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceResponse(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceResponse approveInvoice(String invoiceId){
        Invoice invoice = findInvoiceById(invoiceId);
        if(invoice.getStatus() != 0){
            throw new RuntimeException("Hóa đơn đã được duyệt hoặc đã hủy, không thể duyệt lại!");
        }
        invoice.setStatus(1) ; // = 1 đã thanh toán
        Invoice approvedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceResponse(approvedInvoice);
    }

     @Override
    @Transactional
    public InvoiceResponse updateInvoice(String invoiceId , InvoiceRequest invoiceRequest){
        // Tìm hóa đơn chưa duyệt mới được cập nhật
         Invoice existingInvoice = findInvoiceById(invoiceId);
         if(existingInvoice.getStatus() != 0){
             throw new RuntimeException("Hóa đơn đã được duyệt hoặc đã hủy, không thể cập nhật lại!");
         }
         invoiceMapper.updateInvoiceFromRequest( invoiceRequest ,existingInvoice );
         // cập nhật khách hàng (nếu có )
         if(invoiceRequest.getCustomerId() != null){
             Customer customer = customerRepository.findById(invoiceRequest.getCustomerId())
                     .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với id : " + invoiceRequest.getCustomerId()));
             existingInvoice.setCustomer(customer);
         }

         existingInvoice.getInvoiceDetails().clear();
         BigDecimal totalAmount = BigDecimal.ZERO; // Khởi tạo ví tiền

         if(invoiceRequest.getInvoiceDetails() != null){
             for(InvoiceDetailRequest invoiceDetailRequest : invoiceRequest.getInvoiceDetails()){
                 Product product = productRepository.findById(invoiceDetailRequest.getProductId())
                         .orElseThrow(() -> new RuntimeException("Hàng hóa không tồn tại!"));
                 // tạo chi tiết hóa đơn
                 InvoiceDetail invoiceDetail = new InvoiceDetail();
                 invoiceDetail.setInvoice(existingInvoice);
                 invoiceDetail.setProduct(product);
                 invoiceDetail.setQuantity(invoiceDetailRequest.getQuantity());
                 invoiceDetail.setPrice(invoiceDetailRequest.getPrice());
                 // Tính tiền cộng dồn
                 BigDecimal SubTotal  = invoiceDetail.getSubTotal();
                 totalAmount = totalAmount.add(SubTotal);
                 // Thả chi tiết mới vào giỏ hàng của Phiếu
                 existingInvoice.getInvoiceDetails().add(invoiceDetail);
             }
         }
         // cập nhật lại tổng tiền
         existingInvoice.setTotalAmount(totalAmount);
         // Lưu tất cả
         Invoice updatedInvoice = invoiceRepository.save(existingInvoice);
         return invoiceMapper.toInvoiceResponse(updatedInvoice);
     }

     @Override
    @Transactional
    public InvoiceResponse cancelInvoice( String invoiceId){
        // tìm phiếu
         Invoice invoice = findInvoiceById(invoiceId);
         if(invoice.getStatus() != 0){
             throw new RuntimeException("Hóa đơn đã được duyệt, không thể hủy!");
         }
         if(invoice.getOutboundIssue() != null){
             invoice.setOutboundIssue(null);
         }
         invoice.setStatus(-1);
         Invoice cancelledInvoice = invoiceRepository.save(invoice);
         return invoiceMapper.toInvoiceResponse(cancelledInvoice);
     }

}
