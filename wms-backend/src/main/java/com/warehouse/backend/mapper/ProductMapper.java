package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.ProductRequest;
import com.warehouse.backend.dto.response.ProductResponse;
import com.warehouse.backend.dto.response.MaterialResponse;
import com.warehouse.backend.entity.danhmuc.Product;
import com.warehouse.backend.entity.danhmuc.Material_Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Chuyển entity sang response
    @Mapping(target = "materials", source = "materialProducts")
    // Móc từ bảng LoaiHang ra mã và tên để ném vào Response
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "zoneId", source = "zone.zoneId")
    @Mapping(target = "zoneName", source = "zone.zoneName")
    ProductResponse toProductResponse(Product product);

    // Chuyển Request -> Entity (Bỏ qua danhSachNguyenLieu vì mình lưu bằng tay ở Service)
    @Mapping(target = "materialProducts", ignore = true)
    @Mapping(target = "category" , ignore = true )
    @Mapping(target = "zoneId", source = "zone.zoneId")
    @Mapping(target = "zoneName", source = "zone.zoneName")
    Product toProductEntity(ProductRequest request);

    // Hàm update
    @Mapping(target = "materialProducts", ignore = true)
    @Mapping(target = "category" , ignore = true )
    @Mapping(target = "zoneId", source = "zone.zoneId")
    @Mapping(target = "zoneName", source = "zone.zoneName")
    void updateProductFromRequset(ProductRequest productRequest, @MappingTarget Product product);

    // Dạy MapStruct cách biến 1 dòng NL_H thành 1 dòng NguyenLieuReponse
    default MaterialResponse mapMPToDTO(Material_Product mp) {
        if (mp == null || mp.getMaterial() == null) {
            return null;
        }
        MaterialResponse dto = new MaterialResponse();
        dto.setMaterialId(mp.getMaterial().getMaterialId());
        dto.setMaterialName(mp.getMaterial().getMaterialName());
        return dto;
    }
}
