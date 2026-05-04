package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.CustomerRequest;
import com.warehouse.backend.dto.response.CustomerResponse;
import com.warehouse.backend.entity.danhmuc.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // 1. Chuyển từ Entity -> Response (Để trả về Controller)
    CustomerResponse toCustomerResponse(Customer customer);

    // 2. Chuyển từ Request -> Entity (Để lưu mới vào DB)
    Customer toCustomerEntity(CustomerRequest customerRequest);

    // 3. Hàm siêu việt của MapStruct: Đổ dữ liệu từ Request thẳng vào Entity cũ (Dùng cho Update)
    void updateCustomerFromRequest(CustomerRequest customerRequest, @MappingTarget Customer customer);
}
