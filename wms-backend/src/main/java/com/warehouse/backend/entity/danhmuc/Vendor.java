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
    @Column(name = "MaNCC", columnDefinition = "NVARCHAR(20)")
    private String vendorId;

    @Column(name = "TenNCC", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String vendorName;

    @Column(name = "SoDienThoai", length = 15)
    String phone;

    @Column(name = "Email", length = 100)
    String email;

    @Column(name = "DiaChi", columnDefinition = "NVARCHAR(255)")
    String address;

    @Column(name = "LoaiNCC", columnDefinition = "NVARCHAR(50)")
    String type;

    @Column(name = "TrangThai")
    Integer status = 1; // 1: Đang hợp tác, 0: Ngừng hợp tác
}
