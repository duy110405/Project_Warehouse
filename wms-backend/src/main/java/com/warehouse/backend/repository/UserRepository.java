package com.warehouse.backend.repository;

import com.warehouse.backend.entity.hethong.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Query("SELECT MAX(user.userId) FROM User user")
    String findMaxUserId();
}
