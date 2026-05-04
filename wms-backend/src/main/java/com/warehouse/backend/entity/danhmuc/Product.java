package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HANG") // Tên bảng trong SQL Server
@Getter
@Setter// Tự động tạo Getter, Setter
@ToString(exclude = "chiTietNguyenLieu")// tránh loop
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @Column(name = "MaH", length = 20)
    String productId;

    @Column(name = "TenH", nullable = false, length = 100)
    String productName;

    @Column(name = "SoLg")
    int quantity;

    @Column(name = "Gia")
    BigDecimal price; // Dùng BigDecimal cho DECIMAL(18,2)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLH" , referencedColumnName = "id_category" , columnDefinition = "varchar(20)") // Tên cột khóa ngoại
    Category category;

    // Dòng này giúp Spring Boot tự động móc sang bảng NL_H để lấy nguyên liệu
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<Material_Product> materialProducts = new ArrayList<>();
}
