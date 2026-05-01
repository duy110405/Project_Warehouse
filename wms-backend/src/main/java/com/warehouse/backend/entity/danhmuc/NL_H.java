package com.warehouse.backend.entity.danhmuc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warehouse.backend.entity.nghiepvu.DXuatKho;
import com.warehouse.backend.entity.nghiepvu.PhieuXuat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "NL_H")
@IdClass(NL_H.NL_HId.class) // dùng khóa hỗn hợp(Trỏ vào class nằm bên trong)
@Getter
@Setter
@ToString(exclude = "hang")

public class NL_H {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaH" , referencedColumnName = "MaH" , columnDefinition = "varchar(20)")
    @JsonIgnore // Báo cho thằng tạo JSON biết là "Đừng in thằng Hang này ra nữa, cắt đứt vòng lặp đi"
    private Hang hang;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNL" , referencedColumnName = "MaNL" , columnDefinition = "varchar(20)")
    private NguyenLieu nguyenLieu;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NL_HId implements Serializable {
        private String nguyenLieu;
        private String hang;
    }
}
