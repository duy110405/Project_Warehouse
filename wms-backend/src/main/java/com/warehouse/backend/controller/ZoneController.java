package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.ZoneRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.ZoneRespone;
import com.warehouse.backend.service.IZoneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zone")
@CrossOrigin("*") // Cho phép React gọi API
public class ZoneController {
    private final IZoneService zoneService;

    public ZoneController(IZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping
    public ApiResponse<List<ZoneRespone>> getAllZone(){
        return ApiResponse.<List<ZoneRespone>>builder()
                .code(200)
                .message("lấy danh sách khu thành công")
                .data(zoneService.getAllZone())
                .build();
    }

    @GetMapping("/{zoneId}")
    public ApiResponse<ZoneRespone> getZoneById(@PathVariable String zoneId){
        return ApiResponse.<ZoneRespone>builder()
                .code(200)
                .message("Tìm thấy khu")
                .data(zoneService.getZoneById(zoneId))
                .build();
    }

    @PostMapping
    public ApiResponse<ZoneRespone> createZone(@RequestBody ZoneRequest zoneRequest){
        return ApiResponse.<ZoneRespone>builder()
                .code(201)
                .message("Thêm khu thành công ")
                .data(zoneService.saveZone(zoneRequest))
                .build();
    }

    @PutMapping("/{zoneId}")
    public ApiResponse<ZoneRespone> updateZone(@PathVariable String zoneId , @RequestBody ZoneRequest zoneRequest) {
        return ApiResponse.<ZoneRespone>builder()
                .code(200)
                .message("Cập nhật khu thành công ")
                .data(zoneService.updateZone(zoneId, zoneRequest))
                .build();
    }
    @DeleteMapping("/{zoneId}")
    public ApiResponse<Void> deleteZone(@PathVariable String zoneId){
        zoneService.deleteZone(zoneId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa khu thành công")
                .build();
    }
}
