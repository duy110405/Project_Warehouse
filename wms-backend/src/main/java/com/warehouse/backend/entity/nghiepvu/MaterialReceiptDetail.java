package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Material;
import com.warehouse.backend.entity.danhmuc.Product;
import com.warehouse.backend.entity.danhmuc.Zone;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "DNHAPKHO_NL")
@IdClass(MaterialReceiptDetail.MaterialReceiptDetailId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)
@Getter
@Setter
public class MaterialReceiptDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaPnhapNL" , referencedColumnName = "MaPnhapNL" , columnDefinition = "varchar(20)")
    @ToString.Exclude
    private  InboundMaterialReceipt inboundMaterialReceipt;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaNL" ,  referencedColumnName = "MaNL" , columnDefinition = "varchar(20)")
    private Material material;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhu" , referencedColumnName = "MaKhu" , columnDefinition = "varchar(20)") // Tên cột khóa ngoại
    Zone zone;


    @Column(name = "Gia")
    private BigDecimal price;

    @Column(name = "SoLg")
    private int quantity;

    @Transient // Tính Thành Tiền ảo để dùng trong code/React
    public BigDecimal getSubTotal() {
        if (price != null) {
            return price.multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialReceiptDetailId implements Serializable {
        private String inboundMaterialReceipt;
        private String material;
        private String zone;
    }
}
