package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.OutboundIssueRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.OutboundIssueResponse;
import com.warehouse.backend.service.IOutboundService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outbound")
@CrossOrigin("*") // Cho phép React gọi API
public class OutboundController {
    private final IOutboundService outboundService;

    public OutboundController(IOutboundService outboundService) {
        this.outboundService = outboundService;
    }

    @GetMapping
    public ApiResponse<List<OutboundIssueResponse>> getAllOutboundIssues(){
        return ApiResponse.<List<OutboundIssueResponse>>builder()
                .code(200)
                .message("Lấy danh sách phiếu xuất thành công!")
                .data(outboundService.getAllOutboundIssues())
                .build();
    }

    @GetMapping("/{issueId}")
    public ApiResponse<OutboundIssueResponse> getOutboundIssueById(@PathVariable String issueId){
        return ApiResponse.<OutboundIssueResponse>builder()
                .code(200)
                .message("Tìm thấy phiếu xuất")
                .data(outboundService.getOutBoundById(issueId))
                .build();
    }
    @PostMapping
    public ApiResponse<OutboundIssueResponse> createOutboundIssue(@RequestBody OutboundIssueRequest outboundIssueRequest){
        return ApiResponse.<OutboundIssueResponse>builder()
                .code(201)
                .message("Thêm phiếu xuất thành công!")
                .data(outboundService.saveOutBound(outboundIssueRequest))
                .build();
    }

    @PutMapping("/{issueId}/approve")
    public ApiResponse<OutboundIssueResponse> approveOutboundIssue(@PathVariable String issueId){
        return ApiResponse.<OutboundIssueResponse>builder()
                .code(200)
                .message("Duyệt phiếu xuất thành công!")
                .data(outboundService.approveOutBound(issueId))
                .build();
    }

    @PutMapping("/{issueId}")
    public ApiResponse<OutboundIssueResponse> updateOutboundIssue(@PathVariable String issueId , @RequestBody OutboundIssueRequest outboundIssueRequest){
        return ApiResponse.<OutboundIssueResponse>builder()
                .code(200)
                .message("Cập nhật phiếu xuất thành công!")
                .data(outboundService.updateOutBound(issueId , outboundIssueRequest))
                .build();
    }

    @PutMapping("/{issueId}/cancel")
    public ApiResponse<OutboundIssueResponse> cancelOutboundIssue(@PathVariable String issueId) {
        return ApiResponse.<OutboundIssueResponse>builder()
                .code(200)
                .message("Hủy phiếu xuất thành công!")
                .data(outboundService.cancelOutBound(issueId))
                .build();
    }
}
