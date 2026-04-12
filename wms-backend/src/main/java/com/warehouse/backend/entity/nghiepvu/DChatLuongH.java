package com.warehouse.backend.entity.nghiepvu;


import com.warehouse.backend.entity.danhmuc.Hang;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DCHATLUONGH")
@IdClass(DChatLuongH.DChatLuongHId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)
@Data

public class DChatLuongH {

    @Id
    @ManyToOne
    @JoinColumn(name = "MaPCL", referencedColumnName = "MaPCL", columnDefinition = "varchar(20)")
    @ToString.Exclude
    private PhieuChatLuong phieuChatLuong;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaH", referencedColumnName = "MaH", columnDefinition = "varchar(20)")
    private Hang hang;

    @Column(name = "ChatLg", length = 50)
    private String chatlg;

    @Column(name = "Tinhtrang", length = 20)
    private String tinhtrang;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DChatLuongHId implements Serializable {
        private String phieuChatLuong;
        private String hang;
    }
}
