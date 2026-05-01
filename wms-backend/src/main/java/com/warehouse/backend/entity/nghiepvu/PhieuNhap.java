package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Xuong;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PHIEUNHAP")
@Getter
@Setter
public class PhieuNhap {
    @Id
    @Column(name = "MaPnhap", length = 20)
    private String mapnhap;

    @Column(name = "TenNgNhap", length = 50)
    private String tennguoinhap;

    @Column(name = "Dv", length = 50)
    private String donvi;

    @Column(name = "NgayNh")
    private LocalDate ngaynhap;

    @Column(name = "TongTien", precision = 15, scale = 2)
    private BigDecimal tongtien;

    @Column(name = "TrangThai")
    private Integer trangThai = 0;
// Quy ước ngầm: 0 = Nháp/Chờ duyệt, 1 = Đã hoàn thành, -1 = Đã hủy

    @ManyToOne
    @JoinColumn(name = "MaXuong", referencedColumnName = "MaXuong" , columnDefinition = "varchar(20)")
    private Xuong xuong;

    // Dùng orphanRemoval = true để nếuxóa 1 dòng chi tiết khỏi list, DB cũng tự xóa nó
    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DNhapKho> chiTietPhieuNhap = new ArrayList<>();


}