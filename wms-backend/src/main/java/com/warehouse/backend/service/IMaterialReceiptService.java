package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.MaterialReceiptRequest;
import com.warehouse.backend.dto.response.MaterialReceiptResponse;

import java.util.List;

public interface IMaterialReceiptService {
    String generateNextMaterialReceiptId();
    List<MaterialReceiptResponse> searchReceipts(Integer status, String search, String vendorId);
    MaterialReceiptResponse getMaterialReceiptById(String materialReceiptId);
    MaterialReceiptResponse saveMaterialReceipt(MaterialReceiptRequest materialReceiptRequest);
    MaterialReceiptResponse approveMaterialReceipt(String materialReceiptId);
    MaterialReceiptResponse updateMaterialReceipt(String materialReceiptId , MaterialReceiptRequest materialReceiptRequest);
    MaterialReceiptResponse cancelMaterialReceipt(String materialReceiptId);
}
