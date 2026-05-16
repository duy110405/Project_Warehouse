package com.warehouse.backend.dto.response;

import lombok.Data;

@Data
public class MaterialNormResponse {
    private String materialId;
    private String materialName;
    private String unit;
    private double requiredQuantity;
}