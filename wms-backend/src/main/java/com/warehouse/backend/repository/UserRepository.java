package com.warehouse.backend.repository;

import com.warehouse.backend.entity.hethong.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Query("SELECT MAX(user.userId) FROM User user WHERE user.userId LIKE 'ND%'")
    String findMaxUserId();

    boolean existsByUsername(String username);

    Optional<User> findByUsername(@Param("username") String username);
}
