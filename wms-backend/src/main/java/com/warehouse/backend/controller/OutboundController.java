package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.OutboundIssueRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.OutboundIssueResponse;
import com.warehouse.backend.service.IOutboundService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outbound")
public class OutboundController {
    private final IOutboundService outboundService;

    public OutboundController(IOutboundService outboundService) {
        this.outboundService = outboundService;
    }

    @GetMapping
    public ApiResponse<Page<OutboundIssueResponse>> getOutboundIssues(@RequestParam(required = false) Integer status,
                                                                      @RequestParam (required = false) String search,
                                                                      @RequestParam(defaultValue = "0") int page, // Trang số mấy (Spring mặc định bắt đầu từ 0)
                                                                      @RequestParam(defaultValue = "10") int size ){
        String finalSearch = (search == null || search.trim().isEmpty()) ? null : search.trim();
        Pageable pageable = PageRequest.of(page, size, Sort.by("outboundDate").descending());
        Page<OutboundIssueResponse> data = outboundService.getOutboundIssuess(status , finalSearch , pageable);
        return ApiResponse.<Page<OutboundIssueResponse>>builder()
                .code(200)
                .message("Lấy danh sách phiếu xuất thành công!")
                .data(data)
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
    public ApiResponse<OutboundIssueResponse> createOutboundIssue(@Valid @RequestBody OutboundIssueRequest outboundIssueRequest){
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
    public ApiResponse<OutboundIssueResponse> updateOutboundIssue(@PathVariable String issueId ,@Valid @RequestBody OutboundIssueRequest outboundIssueRequest){
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
