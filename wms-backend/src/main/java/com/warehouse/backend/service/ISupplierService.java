package com.warehouse.backend.service;

import com.warehouse.backend.entity.danhmuc.Supplier;

import java.util.List;

public interface ISupplierService {
    List<Supplier> getAllSupplier();
    Supplier getSupplierById(String maxuong) ;
    Supplier saveSupplier(Supplier supplier) ;
    Supplier updateSupplier(String maxuong , Supplier supplier) ;
    void deleteSupplier(String maxuong);
    String generateNextSupplierId();
}
