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
@Table(name = "KHUVUC") // Tên bảng trong SQL Server
@Getter
@Setter// Tự động tạo Getter, Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Zone {
    @Id
    @Column(name = "MaKhu", length = 20)
    String zoneId;

    @Column(name = "TenKhu", nullable = false, length = 100)
    String zoneName;

    @Column (name = "SucChua", length = 50)
    Integer capacity;

    @Column (name = "LoaiKhu" , length = 50)
    Integer zoneType ;  //  1 : khu vực thành phẩm , 2 : khu vực nguyên liệu

}
