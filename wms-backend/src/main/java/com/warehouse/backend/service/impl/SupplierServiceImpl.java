package com.warehouse.backend.service.impl;

import com.warehouse.backend.entity.danhmuc.Supplier;
import com.warehouse.backend.repository.SupplierRepository;
import com.warehouse.backend.service.ISupplierService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements ISupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<Supplier> getAllSupplier(){return supplierRepository.findAll();}

    @Override
    public Supplier getSupplierById(String supplierId){return supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy xường mã :" + supplierId));}

    @Override
    public Supplier saveSupplier(Supplier supplier){
        if(supplier.getSupplierId() == null || supplier.getSupplierId().isEmpty()){
            supplier.setSupplierId(generateNextSupplierId());
        }
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public Supplier updateSupplier(String supplierId , Supplier supplierRequest){
        Supplier existingSupplier = getSupplierById(supplierId);

        existingSupplier.setSupplierName(supplierRequest.getSupplierName());

        return supplierRepository.save(existingSupplier);
    }

    @Override
    public void deleteSupplier(String supplierId) {
        supplierRepository.deleteById(supplierId);
    }

    @Override
    public String generateNextSupplierId(){
        String maxId = supplierRepository.findMaxSupplierId();
        if (maxId == null) return "X001";
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        return String.format("X%03d", nextNumber);
    }
}
