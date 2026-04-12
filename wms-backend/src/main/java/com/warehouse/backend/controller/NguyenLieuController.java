package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.NguyenLieuRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.NguyenLieuResponse;
import com.warehouse.backend.entity.danhmuc.NguyenLieu;
import com.warehouse.backend.service.INguyenLieuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguyen-lieu")
@CrossOrigin("*")
public class NguyenLieuController {
    private final INguyenLieuService nguyenLieuService;

    public NguyenLieuController(INguyenLieuService nguyenLieuService){
        this.nguyenLieuService = nguyenLieuService ;
    }

    @GetMapping
    public ApiResponse<List<NguyenLieuResponse>> getAllNguyenLieu(){
        return ApiResponse.<List<NguyenLieuResponse>>builder()
                .code(200)
                .message("Lấy danh sách ngueeen liệu thành công!")
                .data(nguyenLieuService.getAllNguyenLieu())
                .build();
    }
    @GetMapping("/{manl}")
    public ApiResponse<NguyenLieuResponse> getNguyenLieuById(@PathVariable String manl){
        return ApiResponse.<NguyenLieuResponse>builder()
                .code(200)
                .message("Tìm thấy nguyên liệu!")
                .data(nguyenLieuService.getNguyenLieuById(manl))
                .build();
    }

    @PostMapping
    public ApiResponse<NguyenLieuResponse> createNguyenLieu(@RequestBody NguyenLieuRequest nguyenLieuRequest){
        return ApiResponse.<NguyenLieuResponse>builder()
                .code(201)
                .message("Thêm nguyên liệu thành công")
                .data(nguyenLieuService.saveNguyenLieu(nguyenLieuRequest))
                .build();
    }

    @PutMapping("/{manl}")
    public ApiResponse<NguyenLieuResponse> updateNguyenLieu(@RequestBody NguyenLieuRequest nguyenLieuRequest , @PathVariable String manl){
        return  ApiResponse.<NguyenLieuResponse>builder()
                .code(200)
                .message("Cập nhật nguyên liệu thành công")
                .data(nguyenLieuService.updateNguyenLieu(manl,nguyenLieuRequest))
                .build();
    }

    @DeleteMapping("/{manl}")
    public ApiResponse<Object> deleteNguyenLieu(@PathVariable String manl) {
        nguyenLieuService.deleteNguyenLieu(manl); // Gọi service để xóa
        return ApiResponse.builder()
                .code(200)
                .message("Đã xóa nguyên liệu thành công!")
                .data(null) // Xóa rồi thì ngăn data để trống
                .build();
    }

}
