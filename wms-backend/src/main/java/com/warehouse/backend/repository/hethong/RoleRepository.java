package com.warehouse.backend.repository.hethong;

import com.warehouse.backend.entity.hethong.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    /**
     * Tìm ID nhóm lớn nhất (để sinh ID tự động)
     * VD: ROLE01, ROLE02, ...
     */
    @Query("SELECT MAX(r.groupId) FROM Role r WHERE r.groupId LIKE 'ROLE%'")
    String findMaxGroupId();
}

