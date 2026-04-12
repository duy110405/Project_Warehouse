package com.warehouse.backend.entity.hethong;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "NGUOIDUNG")
@Data
public class NguoiDung {
    @Id
    @Column(name = "IDND", length = 20)
    private String idnd;

    @Column(name = "TenND", nullable = false, length = 100)
    private String tennd;

    @Column(name = "SDT", nullable = false, length = 15)
    private String sdt;
    @Column(name = "TenDN", nullable = false, length = 50)
    private String tendn;
    @Column(name = "MatKhau", nullable = false, length = 50)
    private String matkhau;
    @Column(name = "ChucVu", nullable = false, length = 50)
    private String chucvu;

    @ManyToOne
    @JoinColumn(name = "IDNhom" , referencedColumnName = "IDNhom" , columnDefinition = "varchar(20)") // Tên cột khóa ngoại
    private NhomNguoiDung nhomNguoiDung;




}
