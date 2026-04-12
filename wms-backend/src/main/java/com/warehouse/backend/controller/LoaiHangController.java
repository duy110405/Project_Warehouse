package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.LoaiHangRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.LoaiHangResponse;
import com.warehouse.backend.mapper.LoaiHangMapper;
import com.warehouse.backend.service.ILoaiHangService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loai-hang")
@CrossOrigin("*") // Cho phép React gọi API
public class LoaiHangController {
    private final ILoaiHangService loaiHangService;


    public LoaiHangController(ILoaiHangService LoaiHangService) {
        this.loaiHangService = LoaiHangService;
    }

    @GetMapping
    public ApiResponse<List<LoaiHangResponse>> getAllLoaiHang (){
        return ApiResponse.<List<LoaiHangResponse>>builder()
                .code(200)
                .message("Tìm thành công")
                .data(loaiHangService.getAllLoaiHang())
                .build();
    }

    @GetMapping("/{malh}")
    public ApiResponse<LoaiHangResponse> getLoaiHangById(@PathVariable String malh){
        return ApiResponse.<LoaiHangResponse>builder()
                .code(200)
                .message("tìm thành công ")
                .data(loaiHangService.getLoaiHangById(malh))
                .build();
    }

    @PostMapping
    public ApiResponse<LoaiHangResponse> createLoaiHang(@RequestBody LoaiHangRequest loaiHangRequest){
        return ApiResponse.<LoaiHangResponse>builder()
                .code(201)
                .message("thêm thành công ")
                .data(loaiHangService.saveLoaiHang(loaiHangRequest))
                .build();
    }

    @PutMapping("/{malh}")
    public ApiResponse<LoaiHangResponse> updateLoaiHang(@RequestBody LoaiHangRequest loaiHangRequest , @PathVariable String malh){
        return ApiResponse.<LoaiHangResponse>builder()
                .code(200)
                .message("Cập nhật thành công")
                .data(loaiHangService.updateLoaiHang(malh , loaiHangRequest))
                .build();
    }

    @DeleteMapping("/{malh}")
    public ApiResponse<LoaiHangResponse> deleteLoaiHang(@PathVariable String malh){
        loaiHangService.deleteLoaiHang(malh);
        return ApiResponse.<LoaiHangResponse>builder()
                .code(200)
                .message("xóa thành công")
                .data(null)
                .build();
    }




}
