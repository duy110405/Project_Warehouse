package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "NGUYENLIEU") // Tên bảng trong SQL Server
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter

public class Material {
    @Id
    @Column(name = "MaNL", columnDefinition = "NVARCHAR(20)")
    private String materialId;

    @Column(name = "anhNL" , columnDefinition = "NVARCHAR(255)")
    private String materialImage;

    @Column(name = "TenNL", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String materialName;

    @Column(name = "DVT", columnDefinition = "NVARCHAR(20)")
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
