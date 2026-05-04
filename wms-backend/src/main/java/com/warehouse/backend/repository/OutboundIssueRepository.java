package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.OutboundIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboundIssueRepository extends JpaRepository<OutboundIssue, String> {
    @Query("SELECT MAX(ob.issueId) FROM OutboundIssue ob")
    String findMaxIssueId();
}
