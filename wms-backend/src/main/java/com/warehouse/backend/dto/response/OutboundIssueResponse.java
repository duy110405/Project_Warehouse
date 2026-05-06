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
    String issuedBy;
    String position;
    LocalDate issueDate;
    List<IssueDetailResponse> issueDetails;
}
