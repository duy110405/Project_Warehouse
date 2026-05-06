package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.InboundReceiptRequest;
import com.warehouse.backend.dto.response.InboundReceiptResponse;

import java.util.List;

public interface IInboundService {
    String generateNextReceiptId();
    List<InboundReceiptResponse> getAllInboundReceipt();
    InboundReceiptResponse getInboundReceiptById(String mapnhap);
    InboundReceiptResponse saveInboundReceipt(InboundReceiptRequest inboundReceiptRequest);
    InboundReceiptResponse approveInboundReceipt(String mapnhap);
    InboundReceiptResponse updateInboundReceipt(String mapnhap , InboundReceiptRequest inboundReceiptRequest);
    InboundReceiptResponse cancelInboundReceipt(String mapnhap);
}
