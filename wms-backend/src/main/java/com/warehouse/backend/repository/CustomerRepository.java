package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã KH
    @Query("SELECT MAX(cu.customerId) FROM Customer cu")
    String findMaxCustomerId();
}
