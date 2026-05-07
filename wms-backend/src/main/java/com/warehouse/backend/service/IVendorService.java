package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.VendorRequest;
import com.warehouse.backend.dto.response.VendorResponse;

import java.util.List;

public interface IVendorService {
    List<VendorResponse> getAllVendor();
    VendorResponse getVendorById(String vendorId);
    VendorResponse saveVendor(VendorRequest vendorRequest);
    VendorResponse updateVendor(String vendorId , VendorRequest vendorRequest);
    void deleteVendor(String vendorId);
    String generateNextVendorId();
}
