package com.warehouse.backend.entity.nghiepvu;


import com.warehouse.backend.entity.danhmuc.Product;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "DCHATLUONGH")
@IdClass(InspectionDetail.InspectionDetailId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)
@Data

public class InspectionDetail {

    @Id
    @ManyToOne
    @JoinColumn(name = "MaPCL", referencedColumnName = "MaPCL", columnDefinition = "varchar(20)")
    @ToString.Exclude
    private QualityInspection qualityInspection;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH", referencedColumnName = "MaH", columnDefinition = "varchar(20)")
    private Product product;

    @Column(name = "ChatLg", length = 50)
    private String qualityGrade;

    @Column(name = "Tinhtrang", length = 20)
    private String condition;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectionDetailId implements Serializable {
        private String qualityInspection;
        private String product;
    }
}
