package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.hethong.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHIEUXUAT")
@Data
@ToString(exclude = "issueDetails")// tránh loop
public class OutboundIssue {

    @Id
    @Column(name = "MaPxuat", length = 20)
    private String issueId;

    @Column(name = "NgayXuat")
    @Temporal(TemporalType.DATE) // Để lưu đúng định dạng ngày trong SQL
    private LocalDate issueDate;

    // Kết nối với Người dùng (IDND)
    @ManyToOne
    @JoinColumn(name = "IDND" ,  referencedColumnName ="IDND" , columnDefinition = "varchar(20)")
    private User user;

    @Column(name = "TrangThai")
    private Integer status = 0; // Thêm trường này: 0 = Nháp, 1 = Đã xuất kho, -1 = Đã hủy

    @Column(name = "TongTien", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // Một Phếu Xuất kho có nhiều dòng chi tiết
    @OneToMany(mappedBy = "outboundIssue", cascade = CascadeType.ALL)
    private List<IssueDetail> issueDetails = new ArrayList<>();

    @OneToMany(mappedBy = "outboundIssue")
    private List<Invoice> invoices = new ArrayList<>();

}
