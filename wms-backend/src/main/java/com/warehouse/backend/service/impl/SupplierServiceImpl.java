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
        supplier.setStatus(1);
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
    public SupplierResponse deleteSupplier(String supplierId) {
        //  đổi status về 0 (Ngừng hợp tác)
        Supplier supplier = findSupplierById(supplierId);
        if (supplier.getStatus() != null && supplier.getStatus() == 0) {
            throw new RuntimeException("xưởng này đã ngừng hoạt động từ trước!");
        }
        supplier.setStatus(0);
        supplierRepository.save(supplier); // Lưu lại thay đổi
        return null;
    }

    @Override
    public String generateNextSupplierId(){
        String maxId = supplierRepository.findMaxSupplierId();
        if (maxId == null) return "X001";
        int nextNumber = Integer.parseInt(maxId.substring(1)) + 1;
        return String.format("X%03d", nextNumber);
    }
}
