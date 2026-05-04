package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Material;
import com.warehouse.backend.entity.danhmuc.Supplier;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHIEUCHATLUONG")
@Data
public class QualityInspection {

    @Id
    @Column(name = "MaPCL", length = 20)
    private String inspectionId;

    @Column(name = "TenNKiem", length = 20)
    private String inspectorName;

    @Column(name = "ChucVu", length = 50)
    private String position;

    @Column(name = "NgayL", length = 20)
    private Date inspectionDate;

    @ManyToOne
    @JoinColumn(name ="MaXuong"  ,  referencedColumnName = "MaXuong" , columnDefinition = "varchar(20)")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "MaNL" ,  referencedColumnName = "MaNL" , columnDefinition = "varchar(20)")
    private Material material;

    // Một Phếu chất lượng có nhiều dòng chi tiết
    @OneToMany(mappedBy = "qualityInspection", cascade = CascadeType.ALL)
    private List<InspectionDetail> inspectionDetails = new ArrayList<>();

}
