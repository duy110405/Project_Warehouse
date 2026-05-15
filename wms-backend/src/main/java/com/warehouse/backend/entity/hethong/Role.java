package com.warehouse.backend.entity.hethong;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "QUYEN")
@Getter
@Setter
public class Role {
    @Id
    @Column(name = "IDQuyen", length = 20)
    private String roleId;

    @Column(name = "TenQuyen", nullable = false, length = 100)
    private String roleName;

}
