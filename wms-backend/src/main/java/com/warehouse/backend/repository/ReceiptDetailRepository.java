package com.warehouse.backend.repository;

public interface ReceiptDetailRepository {
    // Xóa toàn bộ chi tiết nhập kho với phiếu cụ thể
    void deleteByInboundReceipt_ReceiptId(String receiptId);
}
