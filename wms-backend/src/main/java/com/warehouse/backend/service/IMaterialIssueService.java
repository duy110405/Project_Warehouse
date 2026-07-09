package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.MaterialIssueRequest;
import com.warehouse.backend.dto.response.MaterialIssueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMaterialIssueService {
    String generateNextMaterialIssueId();
    Page<MaterialIssueResponse> getMaterialIssues(Integer status , String search, String supplierId , Pageable pageable);
    MaterialIssueResponse getMaterialIssueById(String materialIssueId);
    MaterialIssueResponse saveMaterialIssue(MaterialIssueRequest materialIssueRequest);
    MaterialIssueResponse approveMaterialIssue(String materialIssueId);
    MaterialIssueResponse updateMaterialIssue(String materialIssueId, MaterialIssueRequest materialIssueRequest);
    MaterialIssueResponse cancelMaterialIssue(String materialIssueId);
}

