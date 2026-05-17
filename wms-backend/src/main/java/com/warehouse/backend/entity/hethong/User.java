package com.warehouse.backend.entity.hethong;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "NGUOIDUNG")
@Data
public class User {
    @Id
    @Column(name = "IDND", length = 20)
    private String userId;

    @Column(name = "TenND", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String fullName;

    @Column(name = "SDT", nullable = false, columnDefinition = "NVARCHAR(15)")
    private String phoneNumber;

    @Column(name = "TenDN", nullable = false, length = 50, unique = true) // Thêm unique = true vì tên đăng nhập không được trùng
    private String username;

    @Column(name = "MatKhau", nullable = false, length = 255)
    private String password;

    @Column(name = "ChucVu", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String position; // (Hoặc dùng jobTitle)
    @ManyToOne
    @JoinColumn(name = "IDQuyen" , referencedColumnName = "IDQuyen" , columnDefinition = "varchar(20)")
    private Role role;
}
