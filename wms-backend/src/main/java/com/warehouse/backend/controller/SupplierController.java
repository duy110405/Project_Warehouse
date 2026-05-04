package com.warehouse.backend.controller;

import com.warehouse.backend.dto.response.ApiResponse;
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
    public ApiResponse<List<Supplier>> getAllSupplier(){
        return ApiResponse.<List<Supplier>>builder()
                .code(200)
                .message("lấy danh sách xưởng thành công")
                .data(supplierService.getAllSupplier())
                .build();
    }

    @GetMapping("/{supplierId}")
    public ApiResponse<Supplier> getSupplierById(@PathVariable String supplierId){
        return ApiResponse.<Supplier>builder()
                .code(200)
                .message("Tìm thấy xưởng")
                .data(supplierService.getSupplierById(supplierId))
                .build();
    }

    @PostMapping
    public ApiResponse<Supplier> cretaSupplier(@RequestBody Supplier supplier){
        return ApiResponse.<Supplier>builder()
                .code(201)
                .message("Thêm xưởng thành công ")
                .data(supplierService.saveSupplier(supplier))
                .build();
    }

    @PutMapping("/{supplierId}")
    public ApiResponse<Supplier> updateSupplier(@PathVariable String supplierId , @RequestBody Supplier supplier){
        return ApiResponse.<Supplier>builder()
                .code(200)
                .message("Cập nhật xưởng thành công ")
                .data(supplierService.updateSupplier(supplierId , supplier))
                .build();
    }

    @DeleteMapping("/{supplierId}")
    public ApiResponse<Supplier> deleteSupplier(@PathVariable String supplierId){
        return ApiResponse.<Supplier>builder()
                .code(200)
                .message("Xóa xưởng thành công ")
                .data(null)
                .build();
    }
}
