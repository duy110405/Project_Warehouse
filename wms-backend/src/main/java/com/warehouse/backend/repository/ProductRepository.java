package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(p.productId) FROM Product p") // Dùng tên theo entity để khi thay đổi ở Sql chỉ sưả ở entity
    String findMaxProductId();

}
