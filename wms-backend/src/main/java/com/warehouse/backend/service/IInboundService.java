package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.InboundReceiptRequest;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInboundService {
    String generateNextReceiptId();
    List<InboundReceiptResponse> getAllInboundReceipt();
    InboundReceiptResponse getInboundReceiptById(String mapnhap);
    InboundReceiptResponse saveInboundReceipt(InboundReceiptRequest inboundReceiptRequest);
    InboundReceiptResponse approveInboundReceipt(String mapnhap);
    InboundReceiptResponse updateInboundReceipt(String mapnhap , InboundReceiptRequest inboundReceiptRequest);
    InboundReceiptResponse cancelInboundReceipt(String mapnhap);
    Page<InboundReceiptResponse> searchReceipts(Integer status, String search, String supplierId, Pageable pageable);
}
