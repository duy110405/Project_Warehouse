package com.warehouse.backend.entity.danhmuc;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "KHACHHANG")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Customer {
    @Id
    @Column(name = "MaKH", length = 20)
    private String customerId;

    @Column(name = "TenKH", nullable = false, length = 100)
    private String customerName;

    @Column(name = "DiaChi", length = 20)
    private String address;

    @Column(name = "Sdt", length = 15)
    private String phoneNumber;

    @Column(name = "Stk", length = 20)
    private String bankAccountNumber;
}
