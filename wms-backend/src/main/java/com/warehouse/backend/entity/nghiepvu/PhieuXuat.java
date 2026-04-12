package com.warehouse.backend.entity.nghiepvu;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHIEUXUAT")
@Data

public class PhieuXuat {

    @Id
    @Column(name = "MaPxuat", length = 20)
    private String mapxuat;

    @Column(name = "TenNgXuat", length = 20)
    private String tennguoixuat;

    @Column(name = "Dv", length = 50)
    private String donvi;

    @Column(name = "NgayXuat")
    @Temporal(TemporalType.DATE) // Để lưu đúng định dạng ngày trong SQL
    private Date ngayxuat;

    // Một Phếu Xuất kho có nhiều dòng chi tiết
    @OneToMany(mappedBy = "phieuXuat", cascade = CascadeType.ALL)
    private List<DXuatKho> chiTietPhieuXuat;

    @OneToMany(mappedBy = "phieuXuat")
    private List<HoaDon> danhSachHoaDon;

}
