package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.MaterialReceiptRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.MaterialReceiptResponse;
import com.warehouse.backend.service.IMaterialReceiptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inboundMaterial")
@CrossOrigin("*") // Cho phép React gọi API
public class InboundMaterialController {
    private final IMaterialReceiptService materialReceiptService;

    public InboundMaterialController(IMaterialReceiptService materialReceiptService) {
        this.materialReceiptService = materialReceiptService;
    }

    @GetMapping
    public ApiResponse<List<MaterialReceiptResponse>> getAllMaterialReceipts() {
        return ApiResponse.<List<MaterialReceiptResponse>>builder()
                .code(200)
                .message("Lấy danh sách phiếu nhập nguyên vật liệu thành công!")
                .data(materialReceiptService.getAllMaterialReceipt())
                .build();
    }

    @GetMapping("/{materialReceiptId}")
    public ApiResponse<MaterialReceiptResponse> getMaterialReceiptById(@PathVariable String materialReceiptId) {
        return ApiResponse.<MaterialReceiptResponse>builder()
                .code(200)
                .message("Tìm thấy phiếu nhập nguyên vật liệu")
                .data(materialReceiptService.getMaterialReceiptById(materialReceiptId))
                .build();
    }

    @PostMapping
    public ApiResponse<MaterialReceiptResponse> createMaterialReceipt(@RequestBody MaterialReceiptRequest materialReceiptRequest) {
        return ApiResponse.<MaterialReceiptResponse>builder()
                .code(201)
                .message("Thêm phiếu nhập nguyên vật liệu thành công!")
                .data(materialReceiptService.saveMaterialReceipt(materialReceiptRequest))
                .build();
    }

    @PutMapping("/{materialReceiptId}/approve")
    public ApiResponse<MaterialReceiptResponse> approveMaterialReceipt(@PathVariable String materialReceiptId) {
        return ApiResponse.<MaterialReceiptResponse>builder()
                .code(200)
                .message("Duyệt phiếu nhập nguyên vật liệu thành công!")
                .data(materialReceiptService.approveMaterialReceipt(materialReceiptId))
                .build();
    }

    @PutMapping("/{materialReceiptId}")
    public ApiResponse<MaterialReceiptResponse> updateMaterialReceipt(@PathVariable String materialReceiptId, @RequestBody MaterialReceiptRequest materialReceiptRequest) {
        return ApiResponse.<MaterialReceiptResponse>builder()
                .code(200)
                .message("Cập nhật phiếu nhập nguyên vật liệu thành công!")
                .data(materialReceiptService.updateMaterialReceipt(materialReceiptId, materialReceiptRequest))
                .build();
    }

    @PutMapping("/{materialReceiptId}/cancel")
    public ApiResponse<MaterialReceiptResponse> cancelMaterialReceipt(@PathVariable String materialReceiptId) {
        return ApiResponse.<MaterialReceiptResponse>builder()
                .code(200)
                .message("Hủy phiếu nhập nguyên vật liệu thành công!")
                .data(materialReceiptService.cancelMaterialReceipt(materialReceiptId))
                .build();
    }
}
