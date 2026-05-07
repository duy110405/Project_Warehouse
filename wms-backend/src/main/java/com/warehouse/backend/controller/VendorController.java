package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.SupplierRequest;
import com.warehouse.backend.dto.request.VendorRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.VendorResponse;
import com.warehouse.backend.service.ISupplierService;
import com.warehouse.backend.service.IVendorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin("*") // Cho phép React gọi API
public class VendorController {
    private final IVendorService vendorService;

    public VendorController(IVendorService vendorService) {
        this.vendorService = vendorService;
    }


    @GetMapping
    public ApiResponse<List<VendorResponse>> getAllVendor(){
        return ApiResponse.<List<VendorResponse>>builder()
                .code(200)
                .message("lấy danh sách nhà cung cấpthành công")
                .data(vendorService.getAllVendor())
                .build();
    }

    @GetMapping("/{vendorId}")
    public ApiResponse<VendorResponse> getSupplierById(@PathVariable String vendorId){
        return ApiResponse.<VendorResponse>builder()
                .code(200)
                .message("Tìm thấy nhà cung cấp")
                .data(vendorService.getVendorById(vendorId))
                .build();
    }

    @PostMapping
    public ApiResponse<VendorResponse> cretaVendor(@RequestBody VendorRequest vendorRequest){
        return ApiResponse.<VendorResponse>builder()
                .code(201)
                .message("Thêm nhaf cung cấp thành công ")
                .data(vendorService.saveVendor(vendorRequest))
                .build();
    }

    @PutMapping("/{vendorId}")
    public ApiResponse<VendorResponse> updateVendor(@PathVariable String vendorId , @RequestBody VendorRequest vendorRequest){
        return ApiResponse.<VendorResponse>builder()
                .code(200)
                .message("Cập nhật nhà cung cấp thành công ")
                .data(vendorService.updateVendor(vendorId , vendorRequest))
                .build();
    }

    @DeleteMapping("/{vendorId}")
    public ApiResponse<VendorResponse> deleteVendor(@PathVariable String vendorId){
        vendorService.deleteVendor(vendorId);
        return ApiResponse.<VendorResponse>builder()
                .code(200)
                .message("Xóa nhà cung cấp thành công ")
                .data(null)
                .build();
    }
}
