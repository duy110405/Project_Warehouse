package com.warehouse.backend.entity.danhmuc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "NL_H")
@IdClass(Material_Product.Material_ProductId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)
@Getter
@Setter
@ToString(exclude = "product") // tránh loop vô tận khi in ra JSON

public class Material_Product {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaH" , referencedColumnName = "MaH" , columnDefinition = "varchar(20)")
    @JsonIgnore // Báo cho thằng tạo JSON biết là "Đừng in thằng Hang này ra nữa, cắt đứt vòng lặp đi"
    private Product product;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNL" , referencedColumnName = "MaNL" , columnDefinition = "varchar(20)")
    private Material material;

    // số lượng nguyên liệu cần thiết để tạo ra 1 đơn vị sản phẩm
    @Column(name = "SoLuongDinhMuc")
    private double requiredQuantity;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Material_ProductId implements Serializable {
        private String material;
        private String product;
    }
}
