package com.warehouse.backend.controller;

import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.request.HangRequest;
import com.warehouse.backend.dto.response.HangResponse;
import com.warehouse.backend.mapper.HangMapper;
import com.warehouse.backend.service.IHangService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hang")
@CrossOrigin("*") // Cho phép React gọi API
public class HangController {

    private final IHangService hangService ;

    public HangController(IHangService hangService, HangMapper hangMapper) {
        this.hangService = hangService;
    }

    @GetMapping
    public ApiResponse<List<HangResponse>> getAllKhachHang() {
        return ApiResponse.<List<HangResponse>>builder()
                .code(200)
                .message("Lấy danh sách hàng thành công!")
                .data(hangService.getAllHang())
                .build();
    }

    @GetMapping("/{mah}")
    public ApiResponse<HangResponse> getHangById(@PathVariable String mah){
        return ApiResponse.<HangResponse>builder()
                .code(200)
                .message("Tìm thấy hàng")
                .data(hangService.getHangById(mah))
                .build();
    }

    @PostMapping
    public ApiResponse<HangResponse> createHang(@RequestBody HangRequest hangRequest ) {
        return ApiResponse.<HangResponse>builder()
                .code(201)
                .message("Thêm Hàng và Nguyên liệu thành công!")
                .data(hangService.saveHang(hangRequest))
                .build();
    }

    @PutMapping("/{mah}")
    public ApiResponse<HangResponse> updateHang(@PathVariable String mah, @RequestBody HangRequest hangRequest) {
        return ApiResponse.<HangResponse>builder()
                .code(200)
                .message("Cập nhật Hàng và Nguyên liệu thành công!")
                .data(hangService.updateHang(mah , hangRequest))
                .build();
    }

    @DeleteMapping("/{mah}")
    public  ApiResponse<HangResponse> deleteHang(@PathVariable String mah){
        hangService.deleteHang(mah);
        return ApiResponse.<HangResponse>builder()
                .code(200)
                .message("Xóa hàng thành công")
                .data(null)
                .build();
    }

}
