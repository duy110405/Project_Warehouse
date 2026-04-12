package com.warehouse.backend.controller;

import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.entity.danhmuc.Xuong;
import com.warehouse.backend.service.IXuongService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class XuongController {

    private final IXuongService xuongService ;
    public XuongController (IXuongService xuongService){
        this.xuongService = xuongService;
    }

    @GetMapping
    public ApiResponse<List<Xuong>> getAllXuong (){
        return ApiResponse.<List<Xuong>>builder()
                .code(200)
                .message("lấy danh sách xưởng thành công")
                .data(xuongService.getAllXuong())
                .build();
    }

    @GetMapping("/{maxuong}")
    public ApiResponse<Xuong> getXuongById (@PathVariable String maxuong){
        return ApiResponse.<Xuong>builder()
                .code(200)
                .message("Tìm thấy xưởng")
                .data(xuongService.getXuongById(maxuong))
                .build();
    }

    @PostMapping
    public ApiResponse<Xuong> cretaXuong (@RequestBody Xuong xuong){
        return ApiResponse.<Xuong>builder()
                .code(201)
                .message("Thêm xưởng thành công ")
                .data(xuongService.saveXuong(xuong))
                .build();
    }

    @PutMapping("/{maxuong}")
    public ApiResponse<Xuong> updateXuong(@PathVariable String maxuong , @RequestBody Xuong xuong){
        return ApiResponse.<Xuong>builder()
                .code(200)
                .message("Cập nhật xưởng thành công ")
                .data(xuongService.updateXuong(maxuong , xuong))
                .build();
    }

    @DeleteMapping("/{maxuong}")
    public ApiResponse<Xuong> deleteXuong (@PathVariable String maxuong){
        return ApiResponse.<Xuong>builder()
                .code(200)
                .message("Xóa xưởng thành công ")
                .data(null)
                .build();
    }
}
