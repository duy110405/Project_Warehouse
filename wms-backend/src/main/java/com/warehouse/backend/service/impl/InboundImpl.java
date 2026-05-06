package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.ReceiptDetailRequest;
import com.warehouse.backend.dto.request.InboundReceiptRequest;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import com.warehouse.backend.entity.danhmuc.Product;
import com.warehouse.backend.entity.danhmuc.Supplier;
import com.warehouse.backend.entity.hethong.User;
import com.warehouse.backend.entity.nghiepvu.ReceiptDetail;
import com.warehouse.backend.entity.nghiepvu.InboundReceipt;
import com.warehouse.backend.mapper.InboundMapper;
import com.warehouse.backend.repository.*;
import com.warehouse.backend.service.IInboundService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InboundImpl implements IInboundService {
    private final InboundMapper inboundMapper;
    private final InboundReceiptRepository inboundReceiptRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;


    public InboundImpl(InboundMapper inboundMapper, InboundReceiptRepository inboundReceiptRepository, ReceiptDetailRepository receiptDetailRepository, UserRepository userRepository, ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.inboundMapper = inboundMapper;
        this.inboundReceiptRepository = inboundReceiptRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    public InboundReceipt findInboundReceiptById(String receiptId){
        return inboundReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập với mã : " + receiptId));
    }
    @Override
    public String generateNextReceiptId(){
        String maxId = inboundReceiptRepository.findMaxReceiptId();
        if (maxId == null) return "PN001";
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        return String.format("PN%03d", nextNumber);
    }

    @Override
    public List<InboundReceiptResponse> getAllInboundReceipt(){
        return inboundReceiptRepository.findAll()
                .stream().map(inboundMapper::toInboundResponse).toList();
    }

    @Override
    public InboundReceiptResponse getInboundReceiptById(String receiptId){
        InboundReceipt inboundReceipt = findInboundReceiptById(receiptId);
        return inboundMapper.toInboundResponse(inboundReceipt);
    }

    @Override
    @Transactional
     public InboundReceiptResponse saveInboundReceipt(InboundReceiptRequest inboundReceiptRequest){
        if (inboundReceiptRequest.getReceiptDetails() == null || inboundReceiptRequest.getReceiptDetails().isEmpty()) {
            throw new RuntimeException("phải có ít nhất 1 hàng ");
        }
        // Map DTO sang entity
      InboundReceipt inboundReceipt = inboundMapper.toInboundReceiptEntity(inboundReceiptRequest);
        // tự sinh mã
        inboundReceipt.setReceiptId(generateNextReceiptId());
        inboundReceipt.setReceiptDate(LocalDate.now());

        //Tìm User từ Database và gán vào Phiếu
        User user = userRepository.findById(inboundReceiptRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + inboundReceiptRequest.getUserId()));
        inboundReceipt.setUser(user);

        // Tìm Xưởng và gán vào Phiếu
        Supplier supplier = supplierRepository.findById(inboundReceiptRequest.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found!"));
        inboundReceipt.setSupplier(supplier);

        // xử lý Chi tiết nhập kho
        List<ReceiptDetail> receiptDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ReceiptDetailRequest detailRequest : inboundReceiptRequest.getReceiptDetails()) {
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Hàng hóa không tồn tại!"));

        // Tạo dòng chi tiết
            ReceiptDetail receiptDetail = new ReceiptDetail();
            receiptDetail.setInboundReceipt(inboundReceipt);
            receiptDetail.setProduct(product);
            receiptDetail.setQuantity(detailRequest.getQuantity());
            receiptDetail.setPrice(detailRequest.getPrice());

       // tính tổng tiền
       BigDecimal SubTotal = receiptDetail.getSubTotal();
            // Cộng dồn vào Tổng tiền của tờ phiếu
            totalAmount = totalAmount.add(SubTotal);
            // Bỏ dòng này vào giỏ
            receiptDetails.add(receiptDetail);
    }
        inboundReceipt.setTotalAmount(totalAmount);
        inboundReceipt.setReceiptDetails(receiptDetails);
        InboundReceipt savedInboundReceipt = inboundReceiptRepository.save(inboundReceipt);
        return inboundMapper.toInboundResponse(savedInboundReceipt);
    }

    @Override
    @Transactional
    public InboundReceiptResponse approveInboundReceipt(String receiptId) {
        // tìm phiếu nhập
        InboundReceipt inboundReceipt = inboundReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập với mã: " + receiptId));
        if(inboundReceipt.getStatus() != 0 ){
            throw new RuntimeException("Phiếu nhập đã được duyệt hoặc đã hủy, không thể duyệt lại!");
        }
            inboundReceipt.setStatus(1); // Cập nhật trạng thái thành "Đã duyệt"
            for (ReceiptDetail detail : inboundReceipt.getReceiptDetails()) {
                Product product = detail.getProduct();
                // Cộng dồn kho
                int newQuantity = product.getQuantity() + detail.getQuantity();
                product.setQuantity(newQuantity);
                // Cập nhật Hàng xuống DB
                productRepository.save(product);
            }
        InboundReceipt savedInboundReceipt = inboundReceiptRepository.save(inboundReceipt);
        return inboundMapper.toInboundResponse(savedInboundReceipt);
    }

    @Override
    @Transactional
    public InboundReceiptResponse updateInboundReceipt(String receiptId , InboundReceiptRequest inboundReceiptRequest){
        // Tìm phiếu cũ trong db và phiếu phải ở trạng thái chưa duyệt
        InboundReceipt existingInboundReceipt = inboundReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập với mã: " + receiptId));
        if(existingInboundReceipt.getStatus() != 0 ) {
            throw new RuntimeException("Phiếu nhập đã được duyệt hoặc đã hủy, không thể cập nhật!");
        }
            inboundMapper.updateInboundFromRequest(inboundReceiptRequest, existingInboundReceipt);
        // Cập nhật lại Xưởng nếu FE có gửi Xưởng mới
        if(inboundReceiptRequest.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(inboundReceiptRequest.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Xưởng!"));
            existingInboundReceipt.setSupplier(supplier);
        }

        existingInboundReceipt.getReceiptDetails().clear();
        BigDecimal totalAmount = BigDecimal.ZERO; // Khởi tạo ví tiền

            if(inboundReceiptRequest.getReceiptDetails() != null ){
                for (ReceiptDetailRequest detailRequest : inboundReceiptRequest.getReceiptDetails()) {
                    Product product = productRepository.findById(detailRequest.getProductId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy Hàng!"));
                    // Tạo chi tiết mới
                    ReceiptDetail receiptDetail = new ReceiptDetail();
                    receiptDetail.setInboundReceipt(existingInboundReceipt);
                    receiptDetail.setProduct(product);
                    receiptDetail.setQuantity(detailRequest.getQuantity());
                    receiptDetail.setPrice(detailRequest.getPrice());
                    // Tính tiền cộng dồn
                    BigDecimal subTotal = receiptDetail.getSubTotal();
                    totalAmount = totalAmount.add(subTotal);
                    // Thả chi tiết mới vào giỏ hàng của Phiếu
                    existingInboundReceipt.getReceiptDetails().add(receiptDetail);
                }
            }
        //  Cập nhật lại tổng tiền
        existingInboundReceipt.setTotalAmount(totalAmount);
        // Lưu tất cả (Lưu 1 lần duy nhất)
        InboundReceipt updatedInboundReceipt = inboundReceiptRepository.save(existingInboundReceipt);
        // Trả về kết quả
        return inboundMapper.toInboundResponse(updatedInboundReceipt);
    }

    @Transactional
    @Override
    public InboundReceiptResponse cancelInboundReceipt(String receiptId) {
        // tìm phiếu nhập
       InboundReceipt inboundReceipt = findInboundReceiptById(receiptId);
        if(inboundReceipt.getStatus() != 0 ){
            throw new RuntimeException("Phiếu nhập đã được duyệt, không thể xóa!");
        }
        inboundReceipt.setStatus(-1);
        InboundReceipt canceledInboundReceipt = inboundReceiptRepository.save(inboundReceipt);
        return inboundMapper.toInboundResponse(canceledInboundReceipt);
    }
}
