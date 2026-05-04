package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.CategoryRequest;
import com.warehouse.backend.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {

    List<CategoryResponse> getAllCategory();
    CategoryResponse getCategoryById(String malh);
    CategoryResponse saveCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(String malh , CategoryRequest categoryRequest);
    void deleteCategory(String malh);
    String generateNextCategoryId() ;
}
