package com.warehouse.backend.repository;

import com.warehouse.backend.entity.hethong.User;
import com.warehouse.backend.entity.nghiepvu.InboundReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboundReceiptRepository extends JpaRepository<InboundReceipt, String> {
    @Query("SELECT MAX(ir.receiptId) FROM InboundReceipt ir WHERE ir.receiptId LIKE 'PN%'")
    String findMaxReceiptId();

    // LỌC:
    @Query("SELECT i FROM InboundReceipt i WHERE " +
            "(:status IS NULL OR i.status = :status) AND " +
            "(:search IS NULL OR i.receiptId LIKE %:search%) AND " +
            "(:supplierId IS NULL OR i.supplier.supplierId = :supplierId)")
    List<InboundReceipt> searchInboundReceipts(
            @Param("status") Integer status,
            @Param("search") String search,
            @Param("supplierId") String supplierId
    );

}
