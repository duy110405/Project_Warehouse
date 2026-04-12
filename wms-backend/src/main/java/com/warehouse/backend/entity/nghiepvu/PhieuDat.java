package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.NguyenLieu;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHIEUDAT")
@Data

public class PhieuDat {

    @Id
    @Column(name = "MaPdat", length = 20)
    private String mapdat;

    @Column(name = "TenNbgiao", length = 20)
    private String tennnbgiao;

    @Column(name = "Dv", length = 50)
    private String donvi;

    @Column(name = "NgayDat")
    @Temporal(TemporalType.DATE) // Để lưu đúng định dạng ngày trong SQL
    private Date ngaydat;

    @ManyToOne
    @JoinColumn(name = "MaNL" ,  referencedColumnName = "MaNL" , columnDefinition = "varchar(20)")
    private NguyenLieu nguyenLieu;

    // Một Phếu Đặt gia công có nhiều dòng chi tiết
    @OneToMany(mappedBy = "phieuDat", cascade = CascadeType.ALL)
    private List<DDatGiaCong> chiTietPhieuDat;

}
