package com.liceu.casino.DAO;

import com.liceu.casino.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository <User, Long> {
}
