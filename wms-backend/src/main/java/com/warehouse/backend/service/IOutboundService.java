package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.OutboundIssueRequest;
import com.warehouse.backend.dto.response.OutboundIssueResponse;
import com.warehouse.backend.entity.nghiepvu.OutboundIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOutboundService {
    String generateNextIssueId();
//    List<OutboundIssueResponse> getAllOutboundIssues();
    Page<OutboundIssueResponse> getOutboundIssuess(Integer status, String search , Pageable pageable);
    OutboundIssueResponse getOutBoundById(String issueId);
    OutboundIssueResponse saveOutBound(OutboundIssueRequest outboundIssueRequest);
    OutboundIssueResponse approveOutBound(String issueId);
    OutboundIssueResponse updateOutBound(String issueId , OutboundIssueRequest outboundIssueRequest);
    OutboundIssueResponse cancelOutBound(String issueId);
}
