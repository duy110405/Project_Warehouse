package com.warehouse.backend.entity.nghiepvu;


import com.warehouse.backend.entity.danhmuc.Vendor;
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
@Table(name = "PHIEUNHAP_NL")
@Getter
@Setter
@ToString(exclude = "materialReceiptDetails")// tránh loop
public class InboundMaterialReceipt {
    @Id
    @Column(name = "MaPnhapNL", length = 20)
    private String materialReceiptId;

    @Column(name = "NgayNhNL")
    private LocalDate materialReceiptDate;

    @Column(name = "TongTien", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "TrangThai")
    private Integer status = 0;
    // Quy ước ngầm: 0 = Nháp/Chờ duyệt, 1 = Đã hoàn thành, -1 = Đã hủy
// Kết nối với Người dùng (IDND)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDND" ,  referencedColumnName ="IDND" , columnDefinition = "varchar(20)")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNCC", referencedColumnName = "MaNCC", columnDefinition = "varchar(20)")
    private Vendor vendor;

    // Dùng orphanRemoval = true để nếu xóa 1 dòng chi tiết khỏi list, DB cũng tự xóa nó
    @OneToMany(mappedBy = "inboundMaterialReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialReceiptDetail> materialReceiptDetails = new ArrayList<>();

}
