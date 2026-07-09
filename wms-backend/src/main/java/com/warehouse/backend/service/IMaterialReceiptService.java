package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.MaterialReceiptRequest;
import com.warehouse.backend.dto.response.MaterialReceiptResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMaterialReceiptService {
    String generateNextMaterialReceiptId();
    Page<MaterialReceiptResponse> searchReceipts(Integer status, String search, String vendorId , Pageable pageable);
    MaterialReceiptResponse getMaterialReceiptById(String materialReceiptId);
    MaterialReceiptResponse saveMaterialReceipt(MaterialReceiptRequest materialReceiptRequest);
    MaterialReceiptResponse approveMaterialReceipt(String materialReceiptId);
    MaterialReceiptResponse updateMaterialReceipt(String materialReceiptId , MaterialReceiptRequest materialReceiptRequest);
    MaterialReceiptResponse cancelMaterialReceipt(String materialReceiptId);
}
