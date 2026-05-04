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
@Table(name = "DDATGIACONG")
@IdClass(OutsourceDetail.OutsourceDetailId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)

@Data
public class OutsourceDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaPdat" , referencedColumnName = "MaPdat" , columnDefinition = "varchar(20)")
    @ToString.Exclude
    private OutsourceOrder outsourceOrder;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH" ,  referencedColumnName = "MaH" , columnDefinition = "varchar(20)")
    private Product product;

    @Column(name = "GiaTri")
    private BigDecimal value;

    @Column(name = "Mota" , length = 500)
    private String description;

    @Column(name = "DTkythuat" , length = 500)
    private String technicalSpecs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutsourceDetailId implements Serializable {
        private String outsourceOrder;
        private String product;
    }
}
