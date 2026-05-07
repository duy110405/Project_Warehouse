package com.warehouse.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundIssueRequest {
    String userId;
    LocalDate issueDate;
    List<IssueDetailRequest> issueDetails;
}
