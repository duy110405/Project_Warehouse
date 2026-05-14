package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.MaterialRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.MaterialResponse;
import com.warehouse.backend.service.IMaterialService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
public class MaterialController {
    private final IMaterialService materialService;

    public MaterialController(IMaterialService materialService){
        this.materialService = materialService;
    }

    @GetMapping
    public ApiResponse<List<MaterialResponse>> getAllMaterial(){
        return ApiResponse.<List<MaterialResponse>>builder()
                .code(200)
                .message("Lấy danh sách ngueeen liệu thành công!")
                .data(materialService.getAllMaterial())
                .build();
    }
    @GetMapping("/{materialId}")
    public ApiResponse<MaterialResponse> getNguyenLieuById(@PathVariable String materialId){
        return ApiResponse.<MaterialResponse>builder()
                .code(200)
                .message("Tìm thấy nguyên liệu!")
                .data(materialService.getMaterialById(materialId))
                .build();
    }

    @PostMapping
    public ApiResponse<MaterialResponse> createMaterial(@Valid @RequestBody MaterialRequest materialRequest){
        return ApiResponse.<MaterialResponse>builder()
                .code(201)
                .message("Thêm nguyên liệu thành công")
                .data(materialService.saveMaterial(materialRequest))
                .build();
    }

    @PutMapping("/{materialId}")
    public ApiResponse<MaterialResponse> updateMaterial(@Valid @RequestBody MaterialRequest materialRequest, @PathVariable String materialId){
        return  ApiResponse.<MaterialResponse>builder()
                .code(200)
                .message("Cập nhật nguyên liệu thành công")
                .data(materialService.updateMaterial(materialId, materialRequest))
                .build();
    }

    @DeleteMapping("/{materialId}")
    public ApiResponse<Object> deleteMaterial(@PathVariable String materialId) {
        materialService.deleteMaterial(materialId); // Gọi service để xóa
        return ApiResponse.builder()
                .code(200)
                .message("Đã xóa nguyên liệu thành công!")
                .data(null) // Xóa rồi thì ngăn data để trống
                .build();
    }

    @GetMapping
    public ApiResponse<List<MaterialResponse>> getMaterials(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String zoneId
    ) {
        // Khi Frontend gửi tham số trống (VD: ?search=&zoneId=), Spring có thể hiểu nó là chuỗi rỗng "".
        // Ta cần ép nó về null để câu lệnh (:search IS NULL) trong @Query hoạt động chính xác 100%.
        String finalSearch = (search != null && search.trim().isEmpty()) ? null : search.trim();
        String finalZoneId = (zoneId != null && zoneId.trim().isEmpty()) ? null : zoneId;
        // Gọi Service
        List<MaterialResponse> data = materialService.searchMaterial(finalSearch, finalZoneId);
        // Trả về kết quả bọc trong ApiResponse
        return ApiResponse.<List<MaterialResponse>>builder()
                .code(200)
                .message("Lấy danh sách nguyên liệu thành công")
                .data(data)
                .build();
    }

}
