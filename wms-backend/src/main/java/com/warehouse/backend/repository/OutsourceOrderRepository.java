package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.OutsourceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OutsourceOrderRepository extends JpaRepository<OutsourceOrder, String> {
    @Query("SELECT MAX(os.orderId) FROM OutsourceOrder os")
    String findMaxMaPD();
}
