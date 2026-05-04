package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(s.supplierId) FROM Supplier s")
    String findMaxSupplierId();
}
