package com.warehouse.backend.repository;

import com.warehouse.backend.entity.hethong.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("SELECT MAX(r.roleId) FROM Role r WHERE r.roleId LIKE 'ROLE%'")
    String findMaxRoleId();
}

