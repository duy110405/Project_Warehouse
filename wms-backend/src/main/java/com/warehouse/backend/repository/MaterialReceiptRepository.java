package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.InboundMaterialReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialReceiptRepository extends JpaRepository<InboundMaterialReceipt , String> {
    @Query("SELECT MAX(iM.materialReceiptId) FROM InboundMaterialReceipt iM")
    String findMaxMaterialReceiptId();
}
