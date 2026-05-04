package com.warehouse.backend.entity.nghiepvu;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHIEUXUAT")
@Data

public class OutboundIssue {

    @Id
    @Column(name = "MaPxuat", length = 20)
    private String issueId;

    @Column(name = "TenNgXuat", length = 20)
    private String issuedBy;

    @Column(name = "ChucVu", length = 50)
    private String position;

    @Column(name = "NgayXuat")
    @Temporal(TemporalType.DATE) // Để lưu đúng định dạng ngày trong SQL
    private Date issueDate;

    // Một Phếu Xuất kho có nhiều dòng chi tiết
    @OneToMany(mappedBy = "outboundIssue", cascade = CascadeType.ALL)
    private List<IssueDetail> issueDetails = new ArrayList<>();

    @OneToMany(mappedBy = "outboundIssue")
    private List<Invoice> invoices = new ArrayList<>();

}
