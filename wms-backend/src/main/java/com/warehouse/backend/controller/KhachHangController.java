package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.KhachHangRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.KhachHangResponse;
import com.warehouse.backend.entity.danhmuc.KhachHang;
import com.warehouse.backend.service.IKhachHangService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
@CrossOrigin("*") // Cho phép React gọi API

public class KhachHangController {

    private final IKhachHangService khachHangService ;

    public KhachHangController (IKhachHangService khachHangSevice){
        this.khachHangService = khachHangSevice ;

    }

    @GetMapping
    public ApiResponse<List<KhachHangResponse>> getAllKhachHang() {
        return ApiResponse.<List<KhachHangResponse>>builder()
                .code(200)
                .message("Lấy danh sách khách hàng thành công!")
                .data(khachHangService.getAllKhachHang())
                .build();
    }

    @GetMapping("/{makh}")
    public ApiResponse<KhachHangResponse> getKhachHangById(@PathVariable String makh) {
        return ApiResponse.<KhachHangResponse>builder()
                .code(200)
                .message("Tìm thấy khách hàng!")
                .data(khachHangService.getKhachHangById(makh))
                .build();
    }

    @PostMapping
    public ApiResponse<KhachHangResponse> createKhachHang(@RequestBody KhachHangRequest khachHangRequest) {
        return ApiResponse.<KhachHangResponse>builder()
                .code(201)
                .message("Thêm khách hàng thành công!")
                .data(khachHangService.saveKhachHang(khachHangRequest))
                .build();
    }
    @PutMapping("/{makh}")
    public ApiResponse<KhachHangResponse> updateKhachHang(@PathVariable String makh, @RequestBody KhachHangRequest khachHangRequest) {
        return ApiResponse.<KhachHangResponse>builder()
                .code(200)
                .message("Cập nhật thông tin khách hàng thành công!")
                .data(khachHangService.updateKhachHang(makh, khachHangRequest))
                .build();
    }
    @DeleteMapping("/{makh}")
    public ApiResponse<Object> deleteKhachHang(@PathVariable String makh) {
        khachHangService.deleteKhachHang(makh); // Gọi service để xóa
        return ApiResponse.builder()
                .code(200)
                .message("Đã xóa khách hàng thành công!")
                .data(null) // Xóa rồi thì ngăn data để trống
                .build();
    }

}
