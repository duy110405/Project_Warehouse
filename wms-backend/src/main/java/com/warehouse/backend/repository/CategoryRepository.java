package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(c.categoryId) FROM Category c")
    String findMaxCategoryId();
}
