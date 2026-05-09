package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Supplier;
import com.warehouse.backend.entity.hethong.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "PHIEUXUAT_NL")
@Getter
@Setter
@ToString(exclude = "materialIssueDetails")// tránh loop
public class OutboundMaterialIssue {
    @Id
    @Column(name = "MaPxuatNL", length = 20)
    private String materialIssueId;

    @Column(name = "NgayXuatNL")
    private LocalDate materialIssueDate;

    // Kết nối với Người dùng (IDND)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDND" ,  referencedColumnName ="IDND" , columnDefinition = "varchar(20)")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaXuong", referencedColumnName = "MaXuong", columnDefinition = "varchar(20)")
    private Supplier supplier;

    @Column(name = "TrangThai")
    private Integer status = 0; // Thêm trường này: 0 = Nháp, 1 = Đã xuất kho, -1 = Đã hủy

    @Column(name = "TongTien", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // Một Phếu Xuất kho có nhiều dòng chi tiết
    @OneToMany(mappedBy = "outboundMaterialIssue", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<MaterialIssueDetail> materialIssueDetails = new ArrayList<>();
}
