package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.SupplierRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.SupplierResponse;
import com.warehouse.backend.entity.danhmuc.Supplier;
import com.warehouse.backend.service.ISupplierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@CrossOrigin("*") // Cho phép React gọi API
public class SupplierController {

    private final ISupplierService supplierService;
    public SupplierController(ISupplierService supplierService){
        this.supplierService = supplierService;
    }

    @GetMapping
    public ApiResponse<List<SupplierResponse>> getAllSupplier(){
        return ApiResponse.<List<SupplierResponse>>builder()
                .code(200)
                .message("lấy danh sách xưởng thành công")
                .data(supplierService.getAllSupplier())
                .build();
    }

    @GetMapping("/{supplierId}")
    public ApiResponse<SupplierResponse> getSupplierById(@PathVariable String supplierId){
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .message("Tìm thấy xưởng")
                .data(supplierService.getSupplierById(supplierId))
                .build();
    }

    @PostMapping
    public ApiResponse<SupplierResponse> cretaSupplier(@RequestBody SupplierRequest supplierRequest){
        return ApiResponse.<SupplierResponse>builder()
                .code(201)
                .message("Thêm xưởng thành công ")
                .data(supplierService.saveSupplier(supplierRequest))
                .build();
    }

    @PutMapping("/{supplierId}")
    public ApiResponse<SupplierResponse> updateSupplier(@PathVariable String supplierId , @RequestBody SupplierRequest supplierRequest){
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .message("Cập nhật xưởng thành công ")
                .data(supplierService.updateSupplier(supplierId , supplierRequest))
                .build();
    }

    @DeleteMapping("/{supplierId}")
    public ApiResponse<SupplierResponse> deleteSupplier(@PathVariable String supplierId){
        supplierService.deleteSupplier(supplierId);
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .message("Xóa xưởng thành công ")
                .data(null)
                .build();
    }
}
