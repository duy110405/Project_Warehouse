package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "KHACHHANG")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class Customer {
    @Id
    @Column(name = "MaKH", columnDefinition = "NVARCHAR(20)")
    private String customerId;

    @Column(name = "TenKH", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String customerName;

    @Column(name = "DiaChi", columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "Sdt", columnDefinition = "NVARCHAR(15)")
    private String phoneNumber;

    @Column(name = "Stk", columnDefinition = "NVARCHAR(255)")
    private String bankAccountNumber;
}
