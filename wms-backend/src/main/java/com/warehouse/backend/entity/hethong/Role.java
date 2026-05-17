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
    @Column(name = "IDQuyen", columnDefinition = "NVARCHAR(20)")
    private String roleId;

    @Column(name = "TenQuyen", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String roleName;

}
