package com.liceu.casino.DAO;

import com.liceu.casino.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository <User, String> {
    User findByEmail(String email);
    List<User> findByDni(String dni);
}
