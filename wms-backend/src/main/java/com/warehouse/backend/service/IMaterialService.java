package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.MaterialRequest;
import com.warehouse.backend.dto.response.MaterialResponse;

import java.util.List;

public interface IMaterialService {

    List<MaterialResponse> getAllMaterial();
    MaterialResponse getMaterialById(String manl);
    MaterialResponse saveMaterial(MaterialRequest materialRequest);
    MaterialResponse updateMaterial(String manl , MaterialRequest materialRequest);
    void deleteMaterial(String manl);
    String generateNextMaterialId();
}
