package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.CustomerRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.CustomerResponse;
import com.warehouse.backend.service.ICustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin("*") // Cho phép React gọi API

public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerSevice){
        this.customerService = customerSevice ;

    }

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAllCustomer() {
        return ApiResponse.<List<CustomerResponse>>builder()
                .code(200)
                .message("Lấy danh sách khách hàng thành công!")
                .data(customerService.getAllCustomer())
                .build();
    }

    @GetMapping("/{makh}")
    public ApiResponse<CustomerResponse> getCustomerById(@PathVariable String customerId) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .message("Tìm thấy khách hàng!")
                .data(customerService.getCustomerById(customerId))
                .build();
    }

    @PostMapping
    public ApiResponse<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        return ApiResponse.<CustomerResponse>builder()
                .code(201)
                .message("Thêm khách hàng thành công!")
                .data(customerService.saveCustomer(customerRequest))
                .build();
    }
    @PutMapping("/{customerId}")
    public ApiResponse<CustomerResponse> updateCustomer(@PathVariable String customerId, @RequestBody CustomerRequest customerRequest) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .message("Cập nhật thông tin khách hàng thành công!")
                .data(customerService.updateCustomer(customerId, customerRequest))
                .build();
    }
    @DeleteMapping("/{makh}")
    public ApiResponse<Object> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId); // Gọi service để xóa
        return ApiResponse.builder()
                .code(200)
                .message("Đã xóa khách hàng thành công!")
                .data(null) // Xóa rồi thì ngăn data để trống
                .build();
    }

}
