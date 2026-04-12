package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.NguyenLieu;
import com.warehouse.backend.entity.danhmuc.Xuong;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHIEUNHAP")
@Data
public class PhieuNhap {
    @Id
    @Column(name = "MaPnhap", length = 20)
    private String mapnhap;

    @Column(name = "TenNgNhap", length = 20)
    private String tennguoinhap;

    @Column(name = "Dv", length = 50)
    private String donvi;

    @Column(name = "NgayNh")
    @Temporal(TemporalType.DATE) // Để lưu đúng định dạng ngày trong SQL
    private Date ngaynhap;

    @ManyToOne
    // Thêm referencedColumnName để chỉ đích danh cột khóa chính ở bảng XUONG
    @JoinColumn(name = "MaXuong", referencedColumnName = "MaXuong" , columnDefinition = "varchar(20)")
    private Xuong xuong;

    @ManyToOne
    @JoinColumn(name = "MaNL", referencedColumnName = "MaNL" , columnDefinition = "varchar(20)")
    private NguyenLieu nguyenLieu;

    // Một Phếu nhập kho có nhiều dòng chi tiết
    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL)
    private List<DNhapKho> chiTietPhieuNhap;
}
