package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.SupplierRequest;
import com.warehouse.backend.dto.response.SupplierResponse;
import com.warehouse.backend.entity.danhmuc.Supplier;

import java.util.List;

public interface ISupplierService {
    List<SupplierResponse> getAllSupplier();
    SupplierResponse getSupplierById(String supplierId) ;
    SupplierResponse saveSupplier(SupplierRequest supplierRequest) ;
    SupplierResponse updateSupplier(String supplierId , SupplierRequest supplierRequest) ;
    void deleteSupplier(String supplierId);
    String generateNextSupplierId();
}
