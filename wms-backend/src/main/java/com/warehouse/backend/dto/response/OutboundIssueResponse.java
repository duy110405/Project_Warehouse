package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundIssueResponse {
    String issueId;
    String createBy;
    LocalDate issueDate;
    Integer status;
    BigDecimal totalAmount;
    List<IssueDetailResponse> issueDetails;
    List<String> invoiceIds; // Chỉ cần trả về danh sách Mã Hóa Đơn là đủ
}
