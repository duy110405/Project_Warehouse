package com.warehouse.backend.controller;

import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.request.ProductRequest;
import com.warehouse.backend.dto.response.ProductResponse;
import com.warehouse.backend.dto.response.ProductResponse;
import com.warehouse.backend.mapper.ProductMapper;
import com.warehouse.backend.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
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
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return ApiResponse.<ProductResponse>builder()
                .code(201)
                .message("Thêm Hàng và Nguyên liệu thành công!")
                .data(productService.saveProduct(productRequest))
                .build();
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable String productId,@Valid @RequestBody ProductRequest productRequest) {
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

    @GetMapping
    public ApiResponse<List<ProductResponse>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String zoneId
    ) {
        // Khi Frontend gửi tham số trống (VD: ?search=&zoneId=), Spring có thể hiểu nó là chuỗi rỗng "".
        // Ta cần ép nó về null để câu lệnh (:search IS NULL) trong @Query hoạt động chính xác 100%.
        String finalSearch = (search != null && search.trim().isEmpty()) ? null : search.trim();
        String finalCategoryId = (categoryId != null && categoryId.trim().isEmpty()) ? null : categoryId;
        String finalZoneId = (zoneId != null && zoneId.trim().isEmpty()) ? null : zoneId;
        // Gọi Service
        List<ProductResponse> data = productService.searchProduct(finalSearch , finalCategoryId , finalZoneId);
        // Trả về kết quả bọc trong ApiResponse
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Lấy danh sách nguyên liệu thành công")
                .data(data)
                .build();
    }

}
