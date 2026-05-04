package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.CustomerRequest;
import com.warehouse.backend.dto.response.CustomerResponse;
import com.warehouse.backend.entity.danhmuc.Customer;
import com.warehouse.backend.mapper.CustomerMapper;
import com.warehouse.backend.repository.CustomerRepository;
import com.warehouse.backend.service.ICustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper; // Khai báo MapStruct

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    // Hàm phụ (private) để lấy Entity nội bộ, tránh lặp lại code
    private Customer findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với mã: " + customerId));
    }

    @Override
    public List<CustomerResponse> getAllCustomer(){
        return customerRepository.findAll().stream()
                .map(customerMapper::toCustomerResponse).toList();
    }

    @Override
    public CustomerResponse getCustomerById(String makh){
       Customer customer = findCustomerById(makh);
               return  customerMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse saveCustomer(CustomerRequest customerRequest){
        // 1. Map từ DTO sang Entity
        Customer customer = customerMapper.toCustomerEntity(customerRequest);

        // 2. Tự sinh mã
        customer.setCustomerId(generateNextCustomerId());

        // 3. Lưu xuống DB
        Customer savedCustomer = customerRepository.save(customer);

        // 4. Trả về DTO
        return customerMapper.toCustomerResponse(savedCustomer);

    }
    @Override
    @Transactional // Đảm bảo tính toàn vẹn dữ liệu khi cập nhật
    public CustomerResponse updateCustomer(String customerId, CustomerRequest customerRequest) {
        // 1. Tìm Entity cũ trong DB
        Customer existingCustomer = findCustomerById(customerId);

        // 2. Dùng MapStruct đổ tự động các trường từ Request sang Entity cũ
        customerMapper.updateCustomerFromRequest(customerRequest, existingCustomer);

        // 3. Lưu và map sang Response
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toCustomerResponse(updatedCustomer);

    }

    @Override
    public  void deleteCustomer(String customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public String generateNextCustomerId() {
        String maxId = customerRepository.findMaxCustomerId();
        if (maxId == null) return "KH001";
        // Tách phần số: KH005 -> 5
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        // Định dạng lại thành KH + 3 chữ số: KH006
        return String.format("KH%03d", nextNumber);
    }

}
