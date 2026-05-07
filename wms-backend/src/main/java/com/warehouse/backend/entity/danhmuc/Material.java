package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "NGUYENLIEU") // Tên bảng trong SQL Server
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data

public class Material {
    @Id
    @Column(name = "MaNL", length = 20)
    private String materialId;

    @Column(name = "TenNL", nullable = false, length = 100)
    private String materialName;

    @Column(name = "DVT", length = 20)
    private String unit;

    @Column(name = "Gia")
    private BigDecimal price;

    //  Số lượng tồn kho
    @Column(name = "SoLg")
    private int quantity;

    //  Vị trí lưu trữ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhu" , referencedColumnName = "MaKhu" , columnDefinition = "varchar(20)")
    private Zone zone;
}
