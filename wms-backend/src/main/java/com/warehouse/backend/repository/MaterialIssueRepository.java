package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.OutboundMaterialIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialIssueRepository extends JpaRepository<OutboundMaterialIssue, String> {
    @Query("SELECT MAX(o.materialIssueId) FROM OutboundMaterialIssue o WHERE o.materialIssueId LIKE 'PXNL%'")
    String findMaxMaterialIssueId();

    // LỌC:
    @Query("SELECT o FROM OutboundMaterialIssue o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:search IS NULL OR o.materialIssueId LIKE %:search%) AND " +
            "(:supplierId IS NULL OR o.supplier.supplierId = :supplierId)")
    Page<OutboundMaterialIssue> searchoutboundMaterialIssues(
            @Param("status") Integer status,
            @Param("search") String search,
            @Param("supplierId") String supplierId ,
            Pageable pageable
    );
}

