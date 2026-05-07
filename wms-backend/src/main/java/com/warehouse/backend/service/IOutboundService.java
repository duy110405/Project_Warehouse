package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.OutboundIssueRequest;
import com.warehouse.backend.dto.response.OutboundIssueResponse;
import com.warehouse.backend.entity.nghiepvu.OutboundIssue;

import java.util.List;

public interface IOutboundService {
    String generateNextIssueId();
    List<OutboundIssueResponse> getAllOutboundIssues();
    OutboundIssueResponse getOutBoundById(String issueId);
    OutboundIssueResponse saveOutBound(OutboundIssueRequest outboundIssueRequest);
    OutboundIssueResponse approveOutBound(String issueId);
    OutboundIssueResponse updateOutBound(String issueId , OutboundIssueRequest outboundIssueRequest);
    OutboundIssueResponse cancelOutBound(String issueId);
}
