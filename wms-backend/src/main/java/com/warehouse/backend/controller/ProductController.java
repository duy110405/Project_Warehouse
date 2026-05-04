package com.warehouse.backend.controller;

import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.request.ProductRequest;
import com.warehouse.backend.dto.response.ProductResponse;
import com.warehouse.backend.mapper.ProductMapper;
import com.warehouse.backend.service.IProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@CrossOrigin("*") // Cho phép React gọi API
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProduct() {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Lấy danh sách hàng thành công!")
                .data(productService.getAllProduct())
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable String productId){
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Tìm thấy hàng")
                .data(productService.getProductById(productId))
                .build();
    }

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return ApiResponse.<ProductResponse>builder()
                .code(201)
                .message("Thêm Hàng và Nguyên liệu thành công!")
                .data(productService.saveProduct(productRequest))
                .build();
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest) {
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Cập nhật Hàng và Nguyên liệu thành công!")
                .data(productService.updateProduct(productId , productRequest))
                .build();
    }

    @DeleteMapping("/{productId}")
    public  ApiResponse<ProductResponse> deleteProduct(@PathVariable String productId){
        productService.deleteProduct(productId);
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Xóa hàng thành công")
                .data(null)
                .build();
    }

}
