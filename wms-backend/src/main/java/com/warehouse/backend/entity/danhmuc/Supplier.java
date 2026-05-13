package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "XUONG") // Tên bảng trong SQL Server
@Data
public class Supplier {
    @Id
    @Column(name = "MaXuong", length = 20)
    private String supplierId;

    @Column(name = "TenXuong", nullable = false, length = 100)
    private String supplierName;

    @Column(name = "LoaiXuong", length = 50)
    String type;

    @Column(name = "TrangThai")
    Integer status = 1; // 1: Đang hoạt động , 0: Ngừng hoạt động

}
