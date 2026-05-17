package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.MaterialIssueRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import com.warehouse.backend.dto.response.MaterialIssueResponse;
import com.warehouse.backend.service.IMaterialIssueService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outbound-material")
public class OutboundMaterialController {
    private final IMaterialIssueService materialIssueService;

    public OutboundMaterialController(IMaterialIssueService materialIssueService) {
        this.materialIssueService = materialIssueService;
    }

    @GetMapping
    public ApiResponse<List<MaterialIssueResponse>> getMaterialIssues( @RequestParam(required = false) Integer status,
                                                                       @RequestParam(required = false) String search,
                                                                       @RequestParam(required = false) String supplierId) {
        // Xử lý chuỗi rỗng
        String finalSearch = (search == null || search.trim().isEmpty()) ? null : search.trim();
        String finalSupplier = (supplierId == null || supplierId.trim().isEmpty()) ? null : supplierId.trim();
        // Gọi hàm search có lọc status
        List<MaterialIssueResponse> data = materialIssueService.getMaterialIssues(status ,finalSearch , finalSupplier);
        return ApiResponse.<List<MaterialIssueResponse>>builder()
                .code(200)
                .message("Lấy danh sách phiếu xuất nguyên liệu thành công!")
                .data(data)
                .build();
    }

    @GetMapping("/{materialIssueId}")
    public ApiResponse<MaterialIssueResponse> getMaterialIssueById(@PathVariable String materialIssueId) {
        return ApiResponse.<MaterialIssueResponse>builder()
                .code(200)
                .message("Tìm thấy phiếu xuất nguyên liệu")
                .data(materialIssueService.getMaterialIssueById(materialIssueId))
                .build();
    }

    @PostMapping
    public ApiResponse<MaterialIssueResponse> createMaterialIssue(@Valid @RequestBody MaterialIssueRequest materialIssueRequest) {
        return ApiResponse.<MaterialIssueResponse>builder()
                .code(201)
                .message("Thêm phiếu xuất nguyên liệu thành công!")
                .data(materialIssueService.saveMaterialIssue(materialIssueRequest))
                .build();
    }

    @PutMapping("/{materialIssueId}/approve")
    public ApiResponse<MaterialIssueResponse> approveMaterialIssue(@PathVariable String materialIssueId) {
        return ApiResponse.<MaterialIssueResponse>builder()
                .code(200)
                .message("Duyệt phiếu xuất nguyên liệu thành công!")
                .data(materialIssueService.approveMaterialIssue(materialIssueId))
                .build();
    }

    @PutMapping("/{materialIssueId}")
    public ApiResponse<MaterialIssueResponse> updateMaterialIssue(
            @PathVariable String materialIssueId,
            @Valid @RequestBody MaterialIssueRequest materialIssueRequest) {
        return ApiResponse.<MaterialIssueResponse>builder()
                .code(200)
                .message("Cập nhật phiếu xuất nguyên liệu thành công!")
                .data(materialIssueService.updateMaterialIssue(materialIssueId, materialIssueRequest))
                .build();
    }

    @PutMapping("/{materialIssueId}/cancel")
    public ApiResponse<MaterialIssueResponse> cancelMaterialIssue(@PathVariable String materialIssueId) {
        return ApiResponse.<MaterialIssueResponse>builder()
                .code(200)
                .message("Hủy phiếu xuất nguyên liệu thành công!")
                .data(materialIssueService.cancelMaterialIssue(materialIssueId))
                .build();
    }
}

