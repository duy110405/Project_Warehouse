package com.warehouse.backend.entity.nghiepvu;

import com.warehouse.backend.entity.danhmuc.Customer;
import com.warehouse.backend.entity.hethong.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HOADON")
@Data

public class Invoice {
    @Id
    @Column(name = "MaHD", length = 20)
    private String invoiceId;

    @Column(name = "NgayL")
    private LocalDate invoiceDate;

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
