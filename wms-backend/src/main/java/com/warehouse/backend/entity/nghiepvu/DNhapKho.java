package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Hang;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "DNHAPKHO")
@IdClass(DNhapKho.DNhapKhoId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)
@Getter
@Setter// Tự động tạo Getter, Setter
public class DNhapKho {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaPnhap" , referencedColumnName = "MaPnhap" , columnDefinition = "varchar(20)")
    @ToString.Exclude
    private PhieuNhap phieuNhap;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH" ,  referencedColumnName = "MaH" , columnDefinition = "varchar(20)")
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
    public static class DNhapKhoId implements Serializable {
        private String phieuNhap;
        private String hang;
    }
}
