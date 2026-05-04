package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.InboundReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundReceiptRepository extends JpaRepository<InboundReceipt, String> {
    @Query("SELECT MAX(ir.receiptId) FROM InboundReceipt ir")
    String findMaxReceiptId();

}
