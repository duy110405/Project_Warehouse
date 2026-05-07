package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Customer;
import com.warehouse.backend.entity.hethong.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HOADON")
@Getter
@Setter
@ToString(exclude = "invoiceDetails")// tránh loop
public class Invoice {
    @Id
    @Column(name = "MaHD", length = 20)
    private String invoiceId;

    @Column(name = "NgayL")
    private LocalDate invoiceDate;

    @Column(name = "TongTien", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "Status")
    private Integer status = 0;

    // Kết nối với Khách hàng
    @ManyToOne
    @JoinColumn(name = "MaKH" , referencedColumnName = "MaKH", columnDefinition = "varchar(20)")
    private Customer customer;

    // Kết nối với Người dùng (IDND)
    @ManyToOne
    @JoinColumn(name = "IDND" ,  referencedColumnName ="IDND" , columnDefinition = "varchar(20)")
    private User user;

    @ManyToOne
    @JoinColumn(name = "MaPxuat" ,  referencedColumnName = "MaPxuat" , columnDefinition = "varchar(20)")
    private OutboundIssue outboundIssue;

    // Một hóa đơn có nhiều dòng chi tiết
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceDetail> invoiceDetails = new ArrayList<>();
}
