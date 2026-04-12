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
@Table(name = "DDATGIACONG")
@IdClass(DDatGiaCong.DDatGiaCongId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)

@Data
public class DDatGiaCong {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaPdat" , referencedColumnName = "MaPdat" , columnDefinition = "varchar(20)")
    @ToString.Exclude
    private PhieuDat phieuDat;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH" ,  referencedColumnName = "MaH" , columnDefinition = "varchar(20)")
    private Hang hang;

    @Column(name = "GiaTri")
    private BigDecimal giatri;

    @Column(name = "Mota" , length = 500)
    private String mota ;

    @Column(name = "DTkythuat" , length = 500)
    private String dtkythuat ;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DDatGiaCongId implements Serializable {
        private String phieuDat;
        private String hang;
    }
}
