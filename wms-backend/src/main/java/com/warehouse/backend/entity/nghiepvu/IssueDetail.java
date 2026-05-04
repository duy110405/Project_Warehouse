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
@Table(name = "DXUATKHO")
@IdClass(IssueDetail.IssueDetailId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)
@Data

public class IssueDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaPxuat" , referencedColumnName = "MaPxuat" , columnDefinition = "varchar(20)")
    @ToString.Exclude
    private OutboundIssue outboundIssue;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH" ,  referencedColumnName = "MaH" , columnDefinition = "varchar(20)")
    private Product product;

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
    public static class IssueDetailId implements Serializable {
        private String outboundIssue;
        private String product;
    }
}
