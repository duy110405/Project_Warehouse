package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.ProductRequest;
import com.warehouse.backend.dto.response.ProductResponse;

import java.util.List;

public interface IProductService {
    List<ProductResponse> getAllProduct();
    ProductResponse getProductById(String mah);
    ProductResponse saveProduct(ProductRequest productRequest);
    ProductResponse updateProduct(String mah , ProductRequest productRequest);
    void deleteProduct(String mah);
    String generateNextProductId() ;
}
