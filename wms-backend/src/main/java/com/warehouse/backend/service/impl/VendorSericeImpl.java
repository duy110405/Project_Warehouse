package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.VendorRequest;
import com.warehouse.backend.dto.response.VendorResponse;
import com.warehouse.backend.entity.danhmuc.Vendor;
import com.warehouse.backend.entity.danhmuc.Vendor;
import com.warehouse.backend.mapper.SupplierMapper;
import com.warehouse.backend.mapper.VendorMapper;
import com.warehouse.backend.repository.SupplierRepository;
import com.warehouse.backend.repository.VendorRepository;
import com.warehouse.backend.service.IVendorService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorSericeImpl implements IVendorService {
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorSericeImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }


    private Vendor findVendorById(String vendorId) {return vendorRepository.findById(vendorId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp mã :" + vendorId));}

    @Override
    public List<VendorResponse> getAllVendor(){
        return vendorRepository.findAll().stream().map(vendorMapper::toVendorResponse)
                .toList();
    }

    @Override
    public VendorResponse getVendorById(String vendorId){
        Vendor vendor = findVendorById(vendorId);
        return vendorMapper.toVendorResponse(vendor);
    }

    @Override
    public VendorResponse saveVendor(VendorRequest vendorRequest){
        Vendor vendor = vendorMapper.toVendorEntity(vendorRequest);
        vendor.setVendorId(generateNextVendorId());
        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.toVendorResponse(savedVendor);
    }

    @Override
    @Transactional
    public VendorResponse updateVendor(String vendorId , VendorRequest vendorRequest){
        Vendor existingVendor = findVendorById(vendorId);
        vendorMapper.updateFromRequest(vendorRequest, existingVendor);
        Vendor updatedVendor = vendorRepository.save(existingVendor);
        return vendorMapper.toVendorResponse(updatedVendor);
    }

    @Override
    public void deleteVendor(String vendorId) {
        vendorRepository.deleteById(vendorId);
    }

    @Override
    public String generateNextVendorId(){
        String maxId = vendorRepository.findMaxVendorId();
        if (maxId == null) return "NCC001";
        int nextNumber = Integer.parseInt(maxId.substring(3)) + 1;
        return String.format("NCC%03d", nextNumber);
    }
}
