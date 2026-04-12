package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Hang;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "DHOADON")
@IdClass(DHoaDon.DHoaDonId.class) // Dùng khóa hỗn hợp
@Data
public class DHoaDon {

    @Id
    @ManyToOne
    @JoinColumn(name = "MaHD" , referencedColumnName = "MaHD" , columnDefinition = "varchar(20)")
    @ToString.Exclude
    private HoaDon hoaDon;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH" ,  referencedColumnName ="MaH" , columnDefinition = "varchar(20)")
    private Hang hang;

    @Column(name = "Gia")
    private BigDecimal gia;

    @Column(name = "SoLg")
    private int soluong;

    @Transient // Tính Thành Tiền ảo để dùng trong code/React
    public BigDecimal getThanhTien() {
        if (gia != null) {
            return gia.multiply(new BigDecimal(soluong));
        }
        return BigDecimal.ZERO;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DHoaDonId implements Serializable {
        private String hoaDon; // Khớp với tên biến trong Entity DHoaDon
        private String hang;
    }
}
