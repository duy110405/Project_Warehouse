package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Product;
import com.warehouse.backend.entity.danhmuc.Zone;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "DNHAPKHO")
@IdClass(ReceiptDetail.ReceiptDetailId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)
@Getter
@Setter// Tự động tạo Getter, Setter
public class ReceiptDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaPnhap" , referencedColumnName = "MaPnhap" , columnDefinition = "varchar(20)")
    @ToString.Exclude
    private InboundReceipt inboundReceipt;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH" ,  referencedColumnName = "MaH" , columnDefinition = "varchar(20)")
    private Product product;

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
    public static class ReceiptDetailId implements Serializable {
        private String inboundReceipt;
        private String product;
        private String zone;
    }
}
