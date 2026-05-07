package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.VendorRequest;
import com.warehouse.backend.dto.response.VendorResponse;
import com.warehouse.backend.entity.danhmuc.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorResponse toVendorResponse(Vendor vendor);
    Vendor toVendorEntity (VendorRequest vendorRequest);
    void updateFromRequest(VendorRequest vendorRequest,@MappingTarget Vendor vendor);
}
