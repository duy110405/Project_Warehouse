package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.CustomerRequest;
import com.warehouse.backend.dto.response.CustomerResponse;

import java.util.List;


public interface ICustomerService {
    List<CustomerResponse> getAllCustomer();
    CustomerResponse getCustomerById(String makh);
    CustomerResponse saveCustomer(CustomerRequest customerRequest);
    CustomerResponse updateCustomer(String makh , CustomerRequest customerRequest) ;
    void deleteCustomer(String makh);
    String generateNextCustomerId(); // lệnh tự sinh mã
}
