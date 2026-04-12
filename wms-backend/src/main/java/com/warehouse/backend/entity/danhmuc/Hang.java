package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "HANG") // Tên bảng trong SQL Server
@Getter
@Setter// Tự động tạo Getter, Setter
@ToString(exclude = "chiTietNguyenLieu")// tránh loop
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hang {

    @Id
    @Column(name = "MaH", length = 20)
    String mah;

    @Column(name = "TenH", nullable = false, length = 100)
    String tenh;

    @Column(name = "SoLg")
    int soluong;

    @Column(name = "Gia")
    BigDecimal gia; // Dùng BigDecimal cho DECIMAL(18,2)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLH" , referencedColumnName = "MaLH" , columnDefinition = "varchar(20)") // Tên cột khóa ngoại
    LoaiHang loaiHang;

    // Dòng này giúp Spring Boot tự động móc sang bảng NL_H để lấy nguyên liệu
    @OneToMany(mappedBy = "hang", fetch = FetchType.EAGER)
    List<NL_H> chiTietNguyenLieu;
}
