package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.SupplierRequest;
import com.warehouse.backend.dto.response.SupplierResponse;
import com.warehouse.backend.entity.danhmuc.Supplier;
import com.warehouse.backend.mapper.SupplierMapper;
import com.warehouse.backend.repository.SupplierRepository;
import com.warehouse.backend.service.ISupplierService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SupplierServiceImpl implements ISupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper){
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    private Supplier findSupplierById(String supplierId) {return supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy xường mã :" + supplierId));}

    @Override
    public List<SupplierResponse> getAllSupplier(){
        return supplierRepository.findAll().stream().map(supplierMapper::toSupplierResponse)
                .toList();
    }

    @Override
    public SupplierResponse getSupplierById(String supplierId){
        Supplier supplier = findSupplierById(supplierId);
        return supplierMapper.toSupplierResponse(supplier);
    }

    @Override
    public SupplierResponse saveSupplier(SupplierRequest supplierRequest){
        Supplier supplier = supplierMapper.toSupplierEntity(supplierRequest);
        supplier.setSupplierId(generateNextSupplierId());
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(savedSupplier);
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(String supplierId , SupplierRequest supplierRequest){
      Supplier existingSupplier = findSupplierById(supplierId);
      supplierMapper.updateFromRequest(supplierRequest, existingSupplier);
      Supplier updatedSupplier = supplierRepository.save(existingSupplier);
      return supplierMapper.toSupplierResponse(updatedSupplier);
    }

    @Override
    public void deleteSupplier(String supplierId) {
        supplierRepository.deleteById(supplierId);
    }

    @Override
    public String generateNextSupplierId(){
        String maxId = supplierRepository.findMaxSupplierId();
        if (maxId == null) return "X001";
        int nextNumber = Integer.parseInt(maxId.substring(1)) + 1;
        return String.format("X%03d", nextNumber);
    }
}
