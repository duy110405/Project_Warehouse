package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.MaterialRequest;
import com.warehouse.backend.dto.response.MaterialResponse;
import com.warehouse.backend.entity.danhmuc.Material;
import com.warehouse.backend.mapper.MaterialMapper;
import com.warehouse.backend.repository.MaterialRepository;
import com.warehouse.backend.service.IMaterialService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements IMaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    public MaterialServiceImpl(MaterialRepository materialRepository, MaterialMapper materialMapper){
        this.materialRepository = materialRepository;
        this.materialMapper = materialMapper;
    }

    private Material findMaterialById(String materialId) {return materialRepository.findById(materialId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyeen liệu với mã :"+ materialId));}

    @Override
    public List<MaterialResponse> getAllMaterial(){
        return materialRepository.findAll().stream().map(materialMapper::toMaterialResponse)
                .toList();
    }

    @Override
    public MaterialResponse getMaterialById(String materialId) {
       Material material = findMaterialById(materialId);
       return materialMapper.toMaterialResponse(material);
    }

    @Override
    public MaterialResponse saveMaterial(MaterialRequest materialRequest){
        Material material = materialMapper.toMaterialEntity(materialRequest);
        material.setMaterialId(generateNextMaterialId());
        Material savedMaterial = materialRepository.save(material);
        return materialMapper.toMaterialResponse(savedMaterial);
    }

    @Override
    @Transactional
    public MaterialResponse updateMaterial(String materialId , MaterialRequest materialRequest){
      Material existingMaterial = findMaterialById(materialId);
      materialMapper.updateMaterialFromRequset(materialRequest, existingMaterial);
      Material updateMaterial = materialRepository.save(existingMaterial);
      return materialMapper.toMaterialResponse(updateMaterial);

    }

    @Override
    public void deleteMaterial(String materialId) {
       materialRepository.deleteById(materialId);
    }

    @Override
    public String generateNextMaterialId(){
        String maxId = materialRepository.findMaxMaterialId();
        if(maxId == null ) return "NL001" ;
        int nextNumber = Integer.parseInt(maxId.substring(2)) +1 ;
        return String.format("NL%03d" , nextNumber);
    }
}
