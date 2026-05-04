package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Material_Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Material_ProductRepository extends JpaRepository<Material_Product, Long> {
    // Xóa toàn bộ nguyên liệu thuộc về một mã Hàng cụ thể
    void deleteByProduct_ProductId(String productId);
}
