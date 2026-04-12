package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.KhachHang;
import com.warehouse.backend.entity.hethong.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "HOADON")
@Data

public class HoaDon {
    @Id
    @Column(name = "MaHD", length = 20)
    private String mahd;

    @Column(name = "NgayL")
    private LocalDate ngayl;

    // Kết nối với Khách hàng
    @ManyToOne
    @JoinColumn(name = "MaKH" , referencedColumnName = "MaKH", columnDefinition = "varchar(20)")
    private KhachHang khachHang;

    // Kết nối với Người dùng (IDND)
    @ManyToOne
    @JoinColumn(name = "IDND" ,  referencedColumnName ="IDND" , columnDefinition = "varchar(20)")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "MaPxuat" ,  referencedColumnName = "MaPxuat" , columnDefinition = "varchar(20)")
    private PhieuXuat phieuXuat;

    // Một hóa đơn có nhiều dòng chi tiết
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL)
    private List<DHoaDon> chiTietHoaDon;
}