package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.IssueDetailRequest;
import com.warehouse.backend.dto.request.OutboundIssueRequest;
import com.warehouse.backend.dto.response.OutboundIssueResponse;
import com.warehouse.backend.entity.danhmuc.Product;
import com.warehouse.backend.entity.danhmuc.Zone;
import com.warehouse.backend.entity.hethong.User;
import com.warehouse.backend.entity.nghiepvu.InboundReceipt;
import com.warehouse.backend.entity.nghiepvu.Invoice;
import com.warehouse.backend.entity.nghiepvu.IssueDetail;
import com.warehouse.backend.entity.nghiepvu.OutboundIssue;
import com.warehouse.backend.mapper.OutboundMapper;
import com.warehouse.backend.repository.*;
import com.warehouse.backend.service.IOutboundService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OutboundImpl implements IOutboundService {
    private final OutboundIssueRepository outboundIssueRepository;
    private final OutboundMapper outboundMapper;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ZoneRepository zoneRepository;

    public OutboundImpl(OutboundIssueRepository outboundIssueRepository, OutboundMapper outboundMapper, InvoiceRepository invoiceRepository, UserRepository userRepository, ProductRepository productRepository, ZoneRepository zoneRepository) {
        this.outboundIssueRepository = outboundIssueRepository;
        this.outboundMapper = outboundMapper;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.zoneRepository = zoneRepository;
    }

    public OutboundIssue findOutBoundById(String issueId) {
        return outboundIssueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu xuất kho với ID: " + issueId));
    }
    @Override
    public String generateNextIssueId(){
        String maxId = outboundIssueRepository.findMaxIssueId();
        if (maxId == null) return "PX001";
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        return String.format("PX%03d", nextNumber);
    }


    @Override
    public List<OutboundIssueResponse> getAllOutboundIssues() {
       return  outboundIssueRepository.findAll()
               .stream().map(outboundMapper::toOutboundIssueResponse).toList();
    }
    @Override
    public OutboundIssueResponse getOutBoundById(String issueId){
        OutboundIssue outboundIssue = findOutBoundById(issueId);
        return outboundMapper.toOutboundIssueResponse(outboundIssue);
    }

    @Override
    @Transactional
    public OutboundIssueResponse saveOutBound(OutboundIssueRequest outboundIssueRequest){
        if(outboundIssueRequest.getIssueDetails() == null || outboundIssueRequest.getIssueDetails().isEmpty()){
            throw new RuntimeException("Chi tiết phiếu xuất kho không được để trống");
        }
        OutboundIssue outboundIssue = outboundMapper.toOutboundIssueEntity(outboundIssueRequest);
        outboundIssue.setIssueId(generateNextIssueId());
        outboundIssue.setIssueDate(LocalDate.now());

        //Tìm User từ Database và gán vào Phiếu
        User user = userRepository.findById(outboundIssueRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + outboundIssueRequest.getUserId()));
        outboundIssue.setUser(user);

        // xử lý Chi tiết xuất kho
        List<IssueDetail> issueDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(IssueDetailRequest issueDetailRequest : outboundIssueRequest.getIssueDetails()){
            Product product = productRepository.findById(issueDetailRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Hàng hóa không tồn tại!"));

            // TÌM ZONE LINH HOẠT
            Zone zone = null;
            if (issueDetailRequest.getZoneId() != null) {
                zone = zoneRepository.findById(issueDetailRequest.getZoneId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Khu vực!"));
            } else {
                zone = product.getZone();
            }
                int amountToAdd = issueDetailRequest.getQuantity();

                if ( amountToAdd > product.getQuantity()) {
                    throw new RuntimeException("Không thể xuất phiếu! Sản phẩm " + product.getProductName() +
                            " chỉ còn " + product.getQuantity() + " cái trong kho, nhưng bạn muốn xuất " + amountToAdd + " cái.");
                }

            // Tạo dòng chi tiết
            IssueDetail issueDetail = new IssueDetail();
            issueDetail.setOutboundIssue(outboundIssue);
            issueDetail.setProduct(product);
            issueDetail.setZone(zone);
            issueDetail.setQuantity(issueDetailRequest.getQuantity());
            issueDetail.setPrice(issueDetailRequest.getPrice());

            // tính tổng tiền
            BigDecimal subTotal = issueDetail.getSubTotal();
            // Cộng dồn vào Tổng tiền của tờ phiếu
            totalAmount = totalAmount.add(subTotal);
            // Bỏ dòng này vào giỏ
            issueDetails.add(issueDetail);
        }
        outboundIssue.setTotalAmount(totalAmount);
        outboundIssue.setIssueDetails(issueDetails);
        // GẮN HÓA ĐƠN
        if (outboundIssueRequest.getInvoiceIds() != null && !outboundIssueRequest.getInvoiceIds().isEmpty()) {
            List<Invoice> invoices = invoiceRepository.findAllById(outboundIssueRequest.getInvoiceIds());
            // Vì Invoice giữ khóa ngoại (MaPxuat), ta phải gán Phiếu Xuất này cho từng cái Hóa Đơn
            for (Invoice invoice : invoices) {
                invoice.setOutboundIssue(outboundIssue);
            }
            outboundIssue.setInvoices(invoices);
        }
        OutboundIssue savedOutboundIssue = outboundIssueRepository.save(outboundIssue);
        return outboundMapper.toOutboundIssueResponse(savedOutboundIssue);
    }

    @Override
    @Transactional
    public OutboundIssueResponse approveOutBound(String issueId){
        OutboundIssue outboundIssue = findOutBoundById(issueId);
        if (outboundIssue.getStatus() != 0) {
            throw new RuntimeException("Phiếu này đã được duyệt hoặc đã hủy!");
        }
        for (IssueDetail issueDetail : outboundIssue.getIssueDetails()) {
            Product product = issueDetail.getProduct();

            // Kiểm tra xem kho còn đủ hàng không
            if (product.getQuantity() < issueDetail.getQuantity()) {
                throw new RuntimeException("Không đủ hàng trong kho cho sản phẩm: " + product.getProductName());
            }
            // Thực hiện trừ kho
            product.setQuantity(product.getQuantity() - issueDetail.getQuantity());
            productRepository.save(product);
        }

        //Cập nhật trạng thái Hóa đơn liên quan sang "Hoàn thành" (Status 2)
        if (outboundIssue.getInvoices() != null) {
            for (Invoice invoice : outboundIssue.getInvoices()) {
                invoice.setStatus(2);
                invoiceRepository.save(invoice);
            }
        }
        //Chốt trạng thái phiếu xuất
        outboundIssue.setStatus(1);
        outboundIssue.setIssueDate(LocalDate.now()); // Ghi nhận ngày xuất thực tế

        OutboundIssue savedIssue = outboundIssueRepository.save(outboundIssue);
        return outboundMapper.toOutboundIssueResponse(savedIssue);
    }

    @Override
    @Transactional
    public OutboundIssueResponse updateOutBound(String issueId , OutboundIssueRequest outboundIssueRequest){
        OutboundIssue existingIssue = findOutBoundById(issueId);
        if (existingIssue.getStatus() != 0) {
            throw new RuntimeException("Phiếu này đã được duyệt hoặc đã hủy, không thể chỉnh sửa!");
        }
        outboundMapper.updateFromRequest(outboundIssueRequest, existingIssue);
        existingIssue.getIssueDetails().clear();
        BigDecimal totalAmount = BigDecimal.ZERO; // Khởi tạo ví tiền
        if(outboundIssueRequest.getIssueDetails() != null){
            for(IssueDetailRequest issueDetailRequest : outboundIssueRequest.getIssueDetails()){
                Product product = productRepository.findById(issueDetailRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Hàng!"));

                // TÌM ZONE LINH HOẠT
                Zone zone = null;
                if (issueDetailRequest.getZoneId() != null) {
                    zone = zoneRepository.findById(issueDetailRequest.getZoneId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy Khu vực!"));
                } else {
                    zone = product.getZone();
                }
                int amountToAdd = issueDetailRequest.getQuantity();

                if ( amountToAdd > product.getQuantity()) {
                    throw new RuntimeException("Không thể xuất phiếu! Sản phẩm " + product.getProductName() +
                            " chỉ còn " + product.getQuantity() + " cái trong kho, nhưng bạn muốn xuất " + amountToAdd + " cái.");
                }

                // chi tiết phiếu
                IssueDetail issueDetail = new IssueDetail();
                issueDetail.setOutboundIssue(existingIssue);
                issueDetail.setProduct(product);
                issueDetail.setZone(zone);
                issueDetail.setQuantity(issueDetailRequest.getQuantity());
                issueDetail.setPrice(issueDetailRequest.getPrice());

                BigDecimal subTotal = issueDetail.getSubTotal();
                totalAmount = totalAmount.add(subTotal);
                // Thả chi tiết mới vào giỏ hàng của Phiếu
                existingIssue.getIssueDetails().add(issueDetail);
            }
        }
        //  Cập nhật lại tổng tiền
        existingIssue.setTotalAmount(totalAmount);

        // GỠ HÓA ĐƠN CŨ TRƯỚC KHI GẮN HÓA ĐƠN MỚI ---
        if (existingIssue.getInvoices() != null) {
            for (Invoice oldInvoice : existingIssue.getInvoices()) {
                oldInvoice.setOutboundIssue(null); // Gỡ liên kết cũ
            }
            existingIssue.getInvoices().clear();
        }

        // --- GẮN HÓA ĐƠN MỚI ---
        if (outboundIssueRequest.getInvoiceIds() != null && !outboundIssueRequest.getInvoiceIds().isEmpty()) {
            List<Invoice> invoices = invoiceRepository.findAllById(outboundIssueRequest.getInvoiceIds());
            for (Invoice invoice : invoices) {
                invoice.setOutboundIssue(existingIssue); // Gắn liên kết mới
            }
            existingIssue.setInvoices(invoices);
        }
        // Lưu tất cả (Lưu 1 lần duy nhất)
        OutboundIssue updatedOutBound = outboundIssueRepository.save(existingIssue);
        // Trả về kết quả
        return outboundMapper.toOutboundIssueResponse(updatedOutBound);
    }

    @Override
    @Transactional
    public OutboundIssueResponse cancelOutBound(String issueId) {
        OutboundIssue issue = findOutBoundById(issueId);
        if (issue.getStatus() != 0) {
            throw new RuntimeException("Chỉ có thể hủy phiếu xuất đang ở trạng thái Nháp!");
        }
        // GIẢI PHÓNG HÓA ĐƠN: Tháo các hóa đơn ra khỏi phiếu này để chúng không bị chết chùm
        if (issue.getInvoices() != null) {
            for (Invoice invoice : issue.getInvoices()) {
                invoice.setOutboundIssue(null);
            }
            issue.getInvoices().clear();
        }
        // Chốt trạng thái hủy cho Phiếu xuất
        issue.setStatus(-1);
        OutboundIssue cancelledIssue = outboundIssueRepository.save(issue);

        return outboundMapper.toOutboundIssueResponse(cancelledIssue);
    }

}
