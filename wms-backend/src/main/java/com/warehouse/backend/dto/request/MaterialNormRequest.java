package com.warehouse.backend.dto.request;

import lombok.Data;

@Data
public class MaterialNormRequest {
    private String materialId;
    private double requiredQuantity; // Định mức
}
