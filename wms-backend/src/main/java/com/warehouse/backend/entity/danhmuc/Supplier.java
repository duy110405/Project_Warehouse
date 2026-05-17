package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "XUONG") // Tên bảng trong SQL Server
@Data
public class Supplier {
    @Id
    @Column(name = "MaXuong", columnDefinition = "NVARCHAR(20)")
    private String supplierId;

    @Column(name = "TenXuong", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String supplierName;

    @Column(name = "LoaiXuong", columnDefinition = "NVARCHAR(100)")
    String type;

    @Column(name = "TrangThai")
    Integer status = 1; // 1: Đang hoạt động , 0: Ngừng hoạt động

}
