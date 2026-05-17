package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã KH
    @Query("SELECT MAX(c.customerId) FROM Customer c")
    String findMaxCustomerId();

    // Tìm kiếm tương đối (LIKE) trên Tên, Mã hoặc Số điện thoại
    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.customerId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "c.phoneNumber LIKE CONCAT('%', :keyword, '%')")
    List<Customer> searchCustomers(@Param("keyword") String keyword);
}
