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
@Table(name = "PHIEUNHAP")
@Getter
@Setter
@ToString(exclude = "receiptDetails")// tránh loop
public class InboundReceipt {
    @Id
    @Column(name = "MaPnhap", length = 20)
    private String receiptId;

    @Column(name = "NgayNh")
    private LocalDate receiptDate;

    @Column(name = "TongTien", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "TrangThai")
    private Integer status = 0;
// Quy ước ngầm: 0 = Nháp/Chờ duyệt, 1 = Đã hoàn thành, -1 = Đã hủy
// Kết nối với Người dùng (IDND)
@ManyToOne
@JoinColumn(name = "IDND" ,  referencedColumnName ="IDND" , columnDefinition = "varchar(20)")
private User user;

    @ManyToOne
    @JoinColumn(name = "MaXuong", referencedColumnName = "MaXuong" , columnDefinition = "varchar(20)")
    private Supplier supplier;

    // Dùng orphanRemoval = true để nếu xóa 1 dòng chi tiết khỏi list, DB cũng tự xóa nó
    @OneToMany(mappedBy = "inboundReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptDetail> receiptDetails = new ArrayList<>();

}