package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.NguyenLieu;
import com.warehouse.backend.entity.danhmuc.Xuong;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHIEUCHATLUONG")
@Data
public class PhieuChatLuong {

    @Id
    @Column(name = "MaPCL", length = 20)
    private String mapcl;

    @Column(name = "TenNKiem", length = 20)
    private String tennkiem;

    @Column(name = "Dv", length = 50)
    private String donvi;

    @Column(name = "NgayL", length = 20)
    private Date ngayL;

    @ManyToOne
    @JoinColumn(name ="MaXuong"  ,  referencedColumnName = "MaXuong" , columnDefinition = "varchar(20)")
    private Xuong xuong;

    @ManyToOne
    @JoinColumn(name = "MaNL" ,  referencedColumnName = "MaNL" , columnDefinition = "varchar(20)")
    private NguyenLieu nguyenLieu ;

    // Một Phếu chất lượng có nhiều dòng chi tiết
    @OneToMany(mappedBy = "phieuChatLuong", cascade = CascadeType.ALL)
    private List<DChatLuongH> chiTietPhieuChatLuong;

}
