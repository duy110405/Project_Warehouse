package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.Invoice;
import com.warehouse.backend.entity.nghiepvu.OutboundIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundIssueRepository extends JpaRepository<OutboundIssue, String> {
    @Query("SELECT MAX(ob.issueId) FROM OutboundIssue ob WHERE ob.issueId LIKE 'PX%'")
    String findMaxIssueId();

    // ĐÃ SỬA LẠI DÒNG LIKE DÙNG CONCAT VÀ BẮT CHỮ HOA/THƯỜNG BẰNG LOWER()
    @Query("SELECT o FROM OutboundIssue o WHERE "+
            "(:status IS NULL OR o.status = :status) AND "+
            "(:search IS NULL OR LOWER(o.issueId) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<OutboundIssue> searchOutboundIssue (@Param("status") Integer status,
                                             @Param("search") String search);
}
