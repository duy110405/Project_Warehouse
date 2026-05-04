package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.CategoryRequest;
import com.warehouse.backend.dto.response.CategoryResponse;
import com.warehouse.backend.entity.danhmuc.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Chuyển từ entity sang response
    CategoryResponse toCategoryResponse(Category category);

    // request sang entity
    Category toCategoryEntity(CategoryRequest categoryRequest);

    // Hàm update
    void updateCategoryFromRequest(CategoryRequest categoryRequest, @MappingTarget Category category);
}
