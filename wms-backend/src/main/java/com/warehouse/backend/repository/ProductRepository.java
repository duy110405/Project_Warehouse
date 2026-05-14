package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(p.productId) FROM Product p") // Dùng tên theo entity để khi thay đổi ở Sql chỉ sưả ở entity
    String findMaxProductId();

    @Query("SELECT p FROM Product p WHERE " +
            "(:search IS NULL OR p.name LIKE %:search% OR p.code LIKE %:search%) AND " +
            "(:categoryId IS NULL OR p.category.categoryId = :categoryId) AND " +
            "(:zoneId IS NULL OR p.zone.zoneId = :zoneId)")
    List<Product> searchProducts(
            @Param("search") String search,
            @Param("categoryId") String categoryId,
            @Param("zoneId") String zoneId
    );

}
