package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "NCC") // Tên bảng trong SQL Server
@Getter
@Setter// Tự động tạo Getter, Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vendor {
    @Id
    @Column(name = "MaNCC", length = 20)
    private String vendorId;

    @Column(name = "TenNCC", nullable = false, length = 100)
    private String vendorName;
}
