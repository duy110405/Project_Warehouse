package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "DHOADON")
@IdClass(InvoiceDetail.InvoiceDetailId.class) // Dùng khóa hỗn hợp
@Data
public class InvoiceDetail {

    @Id
    @ManyToOne
    @JoinColumn(name = "MaHD" , referencedColumnName = "MaHD" , columnDefinition = "varchar(20)")
    @ToString.Exclude
    private Invoice invoice;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH" ,  referencedColumnName ="MaH" , columnDefinition = "varchar(20)")
    private Product product;

    @Column(name = "Gia")
    private BigDecimal price;

    @Column(name = "SoLg")
    private int quantity;

    @Transient // Tính Thành Tiền ảo để dùng trong code/React
    public BigDecimal getThanhTien() {
        if (price != null) {
            return price.multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceDetailId implements Serializable {
        private String invoice;
        private String product;
    }
}
