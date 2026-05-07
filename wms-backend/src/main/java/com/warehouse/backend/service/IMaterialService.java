package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.MaterialRequest;
import com.warehouse.backend.dto.response.MaterialResponse;

import java.util.List;

public interface IMaterialService {

    List<MaterialResponse> getAllMaterial();
    MaterialResponse getMaterialById(String materialId);
    MaterialResponse saveMaterial(MaterialRequest materialRequest);
    MaterialResponse updateMaterial(String materialId , MaterialRequest materialRequest);
    void deleteMaterial(String materialId);
    String generateNextMaterialId();
}
