package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Material;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHIEUDAT")
@Data

public class OutsourceOrder {

    @Id
    @Column(name = "MaPdat", length = 20)
    private String orderId;

    @Column(name = "TenNbgiao", length = 20)
    private String handedOverBy;

    @Column(name = "ChucVu", length = 50)
    private String poisition;

    @Column(name = "NgayDat")
    @Temporal(TemporalType.DATE) // Để lưu đúng định dạng ngày trong SQL
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "MaNL" ,  referencedColumnName = "MaNL" , columnDefinition = "varchar(20)")
    private Material material;

    // Một Phếu Đặt gia công có nhiều dòng chi tiết
    @OneToMany(mappedBy = "outsourceOrder", cascade = CascadeType.ALL)
    private List<OutsourceDetail> outsourceDetails = new ArrayList<>();

}
