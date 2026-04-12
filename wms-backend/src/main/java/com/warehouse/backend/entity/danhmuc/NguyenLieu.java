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

public class NguyenLieu {
    @Id
    @Column(name = "MaNL", length = 20)
    String manl;

    @Column(name = "TenNL", nullable = false, length = 100)
    String tennl;

    @Column(name = "DVT",length = 20)
    String dvt;

    @Column(name = "Gia")
    BigDecimal gia;

}
