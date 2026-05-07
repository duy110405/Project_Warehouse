package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundIssueResponse {
    String issueId;
    String userId;
    String fullName;
    LocalDate issueDate;
    List<IssueDetailResponse> issueDetails;
    List<String> invoiceIds; // Chỉ cần trả về danh sách Mã Hóa Đơn là đủ
}
