package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.InboundMaterialReceipt;
import com.warehouse.backend.entity.nghiepvu.InboundReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialReceiptRepository extends JpaRepository<InboundMaterialReceipt , String> {
    @Query("SELECT MAX(iM.materialReceiptId) FROM InboundMaterialReceipt iM")
    String findMaxMaterialReceiptId();


    // LỌC:
    @Query("SELECT i FROM InboundMaterialReceipt i WHERE " +
            "(:status IS NULL OR i.status = :status) AND " +
            "(:search IS NULL OR i.materialReceiptId LIKE %:search%) AND " +
            "(:vendorId IS NULL OR i.vendor.vendorId = :vendorId)")
    List<InboundMaterialReceipt> searchInboundMaterialReceipts(
            @Param("status") Integer status,
            @Param("search") String search,
            @Param("vendorId") String vendorId
    );

}
