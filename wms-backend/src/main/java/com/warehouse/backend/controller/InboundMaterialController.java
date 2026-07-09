package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.MaterialReceiptRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import com.warehouse.backend.dto.response.MaterialReceiptResponse;
import com.warehouse.backend.service.IMaterialReceiptService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbound-material")
public class InboundMaterialController {
    private final IMaterialReceiptService materialReceiptService;

    public InboundMaterialController(IMaterialReceiptService materialReceiptService) {
        this.materialReceiptService = materialReceiptService;
    }

    @GetMapping
    public ApiResponse<Page<MaterialReceiptResponse>> getReceipts(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String vendorId ,
            @RequestParam(defaultValue = "0") int page, // Trang số mấy (Spring mặc định bắt đầu từ 0)
            @RequestParam(defaultValue = "10") int size
    ) {
        // Xử lý chuỗi rỗng
        String finalSearch = (search == null || search.trim().isEmpty()) ? null : search.trim();
        String finalVendor = (vendorId == null || vendorId.trim().isEmpty()) ? null : vendorId.trim();
        Pageable pageable = PageRequest.of(page, size, Sort.by("materialReceiptDate").descending());
        // Gọi hàm search có lọc status
        Page<MaterialReceiptResponse> data = materialReceiptService.searchReceipts(status, finalSearch, finalVendor , pageable);
        return ApiResponse.<Page<MaterialReceiptResponse>>builder()
                .code(200)
                .message("Lấy danh sách thành công")
                .data(data)
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
    public ApiResponse<MaterialReceiptResponse> createMaterialReceipt(@Valid @RequestBody MaterialReceiptRequest materialReceiptRequest) {
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
    public ApiResponse<MaterialReceiptResponse> updateMaterialReceipt(@PathVariable String materialReceiptId,@Valid @RequestBody MaterialReceiptRequest materialReceiptRequest) {
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
