package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.SupplierRequest;
import com.warehouse.backend.dto.response.SupplierResponse;
import com.warehouse.backend.entity.danhmuc.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    SupplierResponse toSupplierResponse (Supplier supplier);
    Supplier toSupplierEntity(SupplierRequest supplierRequest);
    void updateFromRequest(SupplierRequest supplierRequest , @MappingTarget Supplier supplier);
}
