package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.MaterialIssueRequest;
import com.warehouse.backend.dto.response.MaterialIssueResponse;

import java.util.List;

public interface IMaterialIssueService {
    String generateNextMaterialIssueId();
    List<MaterialIssueResponse> getMaterialIssues(Integer status ,String search, String supplierId);
    MaterialIssueResponse getMaterialIssueById(String materialIssueId);
    MaterialIssueResponse saveMaterialIssue(MaterialIssueRequest materialIssueRequest);
    MaterialIssueResponse approveMaterialIssue(String materialIssueId);
    MaterialIssueResponse updateMaterialIssue(String materialIssueId, MaterialIssueRequest materialIssueRequest);
    MaterialIssueResponse cancelMaterialIssue(String materialIssueId);
}

