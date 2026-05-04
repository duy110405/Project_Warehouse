package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.CategoryRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.CategoryResponse;
import com.warehouse.backend.service.ICategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin("*") // Cho phép React gọi API
public class CategoryController {
    private final ICategoryService categoryService;


    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategory(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Tìm thành công")
                .data(categoryService.getAllCategory())
                .build();
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable String categoryId){
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("tìm thành công ")
                .data(categoryService.getCategoryById(categoryId))
                .build();
    }

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest){
        return ApiResponse.<CategoryResponse>builder()
                .code(201)
                .message("thêm thành công ")
                .data(categoryService.saveCategory(categoryRequest))
                .build();
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> updateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable String categoryId){
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Cập nhật thành công")
                .data(categoryService.updateCategory(categoryId , categoryRequest))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> deleteCategory(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("xóa thành công")
                .data(null)
                .build();
    }




}
