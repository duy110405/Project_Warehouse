package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "LOAIHANG")
@Data
public class Category {

    @Id
    @Column(name = "MaLH", length = 20)
    private String categoryId;

    @Column(name = "TenLH", nullable = false, length = 100)
    private String categoryName;
}
