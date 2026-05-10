package com.warehouse.backend.repository.hethong;

import com.warehouse.backend.entity.hethong.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT MAX(u.userId) FROM User u WHERE u.userId LIKE 'ND%'")
    String findMaxUserId();

    boolean existsByUsername(String username);
}

