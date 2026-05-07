package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Supplier;
import com.warehouse.backend.entity.danhmuc.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendorRepository extends JpaRepository<Vendor, String>{
    @Query("SELECT MAX(v.vendorId) FROM Vendor v")
    String findMaxVendorId();
}
