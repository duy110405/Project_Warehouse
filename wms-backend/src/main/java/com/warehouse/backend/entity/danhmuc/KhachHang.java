package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "KHACHHANG")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class KhachHang {

    @Id
    @Column(name = "MaKH", length = 20)
    String makh;

    @Column(name = "TenKH", nullable = false, length = 100)
    String tenkh;

    @Column(name = "DiaChi", length = 20)
    String diachi;

    @Column(name = "Sdt", length = 15)
    String sdt;

    @Column(name = "Stk", length = 20)
    String stk;
}
