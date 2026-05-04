package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.CategoryRequest;
import com.warehouse.backend.dto.response.CategoryResponse;
import com.warehouse.backend.entity.danhmuc.Category;
import com.warehouse.backend.mapper.CategoryMapper;
import com.warehouse.backend.repository.CategoryRepository;
import com.warehouse.backend.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category findCategoryById(String categoryId) {return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy loại hàng với mã : " + categoryId));}

    @Override
    public List<CategoryResponse> getAllCategory(){return categoryRepository.findAll()
            .stream().map(categoryMapper::toCategoryResponse).toList();}

    @Override
    public CategoryResponse getCategoryById(String categoryId) {
        Category category = findCategoryById(categoryId);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse saveCategory(CategoryRequest categoryRequest){
      // map Dto sang entity
        Category category = categoryMapper.toCategoryEntity(categoryRequest);
        // tự sinh mã
        category.setCategoryId(generateNextCategoryId());
        // lưu xuống db
        Category savedCategory = categoryRepository.save(category);
        // trả về Dto
        return categoryMapper.toCategoryResponse(savedCategory);
    }
    @Override
    public CategoryResponse updateCategory(String categoryId , CategoryRequest categoryRequest){
        // tìm entity cũ trong db
        Category existingCategory = findCategoryById(categoryId);
        // đổ request vào entity cũ
        categoryMapper.updateCategoryFromRequest(categoryRequest, existingCategory);
        //lưu và map sang response
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toCategoryResponse(updatedCategory);

    }

    @Override
    public void deleteCategory(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public String generateNextCategoryId(){
        String maxId = categoryRepository.findMaxCategoryId();
        if(maxId == null ) return "LH001" ;
        int nextNumber = Integer.parseInt(maxId.substring(2)) +1 ;
        return String.format("LH%03d" , nextNumber);
    }
}
