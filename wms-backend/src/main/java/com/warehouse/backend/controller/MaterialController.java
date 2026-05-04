package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.MaterialRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.MaterialResponse;
import com.warehouse.backend.service.IMaterialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
@CrossOrigin("*")
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
    public ApiResponse<MaterialResponse> createMaterial(@RequestBody MaterialRequest materialRequest){
        return ApiResponse.<MaterialResponse>builder()
                .code(201)
                .message("Thêm nguyên liệu thành công")
                .data(materialService.saveMaterial(materialRequest))
                .build();
    }

    @PutMapping("/{materialId}")
    public ApiResponse<MaterialResponse> updateMaterial(@RequestBody MaterialRequest materialRequest, @PathVariable String materialId){
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

}
