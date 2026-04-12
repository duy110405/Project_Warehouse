package com.warehouse.backend.entity.hethong;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "NHOMNGUOIDUNG")
@Data
public class NhomNguoiDung {
    @Id
    @Column(name = "IDNhom", length = 20)
    private String idnhom;

    @Column(name = "TenNhom", nullable = false, length = 100)
    private String tennhom;

    @Column(name = "Quyen", nullable = false, length = 100)
    private String quyen;

}
