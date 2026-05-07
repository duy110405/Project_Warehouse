package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.MaterialNormRequest;
import com.warehouse.backend.dto.request.ProductRequest;
import com.warehouse.backend.dto.response.ProductResponse;
import com.warehouse.backend.entity.danhmuc.*;
import com.warehouse.backend.mapper.ProductMapper;
import com.warehouse.backend.repository.*;
import com.warehouse.backend.service.IProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final MaterialRepository materialRepository;
    private final Material_ProductRepository nlHRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ZoneRepository zoneRepository;


    public ProductServiceImpl(ProductRepository productRepository,
                              MaterialRepository materialRepository,
                              Material_ProductRepository nlHRepository, CategoryRepository categoryRepository, ProductMapper productMapper, ZoneRepository zoneRepository) {
        this.productRepository = productRepository;
        this.materialRepository = materialRepository;
        this.nlHRepository = nlHRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.zoneRepository = zoneRepository;
    }

    private Product findProductById(String productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng với mã : " + productId));
    }

    @Override
    public List<ProductResponse> getAllProduct(){
        return productRepository.findAll()
                .stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    public ProductResponse getProductById(String productId){
        Product product = findProductById(productId);
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse saveProduct(ProductRequest productRequest) {
        // Map DTO sang entity
        Product product = productMapper.toProductEntity(productRequest);
        // tự sinh mã
        product.setProductId(generateNextProductId());
        product.setQuantity(0); // Tồn kho ban đầu luôn bằng 0

        // XỬ LÝ KHU VỰC & KIỂM TRA ZONETYPE (Fail-Fast)
        if (productRequest.getZoneId() != null) {
            Zone zone = zoneRepository.findById(productRequest.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Khu vực lưu trữ!"));

            // Ép buộc phải là Khu Thành Phẩm ( 1 = Thành Phẩm, 2 = Nguyên Liệu)
            if (zone.getZoneType() != 1) {
                throw new RuntimeException("Lỗi: Không thể cất Thành Phẩm vào Khu Nguyên Liệu. Vui lòng chọn đúng Khu Thành Phẩm!");
            }
            product.setZone(zone);
        } else {
            throw new RuntimeException("Bắt buộc phải chọn Khu vực lưu trữ cho sản phẩm!");
        }

        //  Xử lý tìm và gán Loại Hàng trước khi lưu
        if (productRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy loại hàng với mã: " + productRequest.getCategoryId()));
            // Gán vào Entity Hàng
            product.setCategory(category);
        }
        // lưu xuống db
        Product savedProduct = productRepository.save(product);
        // Xử lý lưu bảng NL_H
        if (productRequest.getMaterialIds() != null && !productRequest.getMaterialIds().isEmpty()) {
            for (MaterialNormRequest normRequest : productRequest.getMaterialIds()) {
                Material nl = materialRepository.findById(normRequest.getMaterialId()).orElseThrow();
                Material_Product nlh = new Material_Product();
                nlh.setProduct(savedProduct);
                nlh.setMaterial(nl);
                nlh.setRequiredQuantity(normRequest.getRequiredQuantity());
                nlHRepository.save(nlh);
                savedProduct.getMaterialProducts().add(nlh);
            }
        }
        // Trả về response
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(String productId, ProductRequest productRequest) {

        // Tìm Hàng cũ trong DB
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Hàng với mã: " + productId));

        // dùng mapper cập nhật thông tin
        productMapper.updateProductFromRequset(productRequest, existingProduct);

        //Cập nhật và Validate Zone (Khu vực)
        if (productRequest.getZoneId() != null) {
            Zone zone = zoneRepository.findById(productRequest.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Khu vực lưu trữ!"));
            if (zone.getZoneType() != 1) {
                throw new RuntimeException("Lỗi: Không thể cất Thành Phẩm vào Khu Nguyên Liệu!");
            }
            existingProduct.setZone(zone);
        }

        //Cập nhật Loại Hàng nếu có thay đổi
        if (productRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy loại hàng với mã: " + productRequest.getCategoryId()));
            existingProduct.setCategory(category);
        }

        Product updatedProduct = productRepository.save(existingProduct);

        //Xóa NL cũ - Thêm NL mới
        if (productRequest.getMaterialIds() != null) {
            nlHRepository.deleteByProduct_ProductId(productId);
            for (MaterialNormRequest normRequest : productRequest.getMaterialIds()) {
                Material nl = materialRepository.findById(normRequest.getMaterialId()).orElseThrow();
                Material_Product nlh = new Material_Product();
                nlh.setProduct(updatedProduct);
                nlh.setMaterial(nl);
                nlh.setRequiredQuantity(normRequest.getRequiredQuantity());
                nlHRepository.save(nlh);
            }
        }

        Product finalProduct = productRepository.findById(productId).get();
        return productMapper.toProductResponse(finalProduct);

    }

    @Transactional
    @Override
    public void deleteProduct(String productId) {
        //  Xóa hết các bản ghi liên quan trong bảng con NL_H trước
        nlHRepository.deleteByProduct_ProductId(productId);

        // Sau đó mới xóa an toàn ở bảng cha HANG
        productRepository.deleteById(productId);
    }

    @Override
    public String generateNextProductId(){
        String maxId = productRepository.findMaxProductId();
        if (maxId == null) return "H001";
        int nextNumber = Integer.parseInt(maxId.substring(1)) + 1;
        return String.format("H%03d", nextNumber);
    }
}
