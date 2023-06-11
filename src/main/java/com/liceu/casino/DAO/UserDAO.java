package com.liceu.casino.DAO;

import com.liceu.casino.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO extends JpaRepository <User, String> {
    User findByEmail(String email);
    List<User> findByDni(String dni);
    @Modifying
    @Transactional
    @Query("UPDATE User SET coins = :coins WHERE email = :email")
    void updateUser(@Param("coins") Long coins, @Param("email") String email);

}
