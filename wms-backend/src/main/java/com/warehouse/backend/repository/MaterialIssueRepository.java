package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.OutboundMaterialIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialIssueRepository extends JpaRepository<OutboundMaterialIssue, String> {
    @Query("SELECT MAX(o.materialIssueId) FROM OutboundMaterialIssue o WHERE o.materialIssueId LIKE 'PXNL%'")
    String findMaxMaterialIssueId();
}

