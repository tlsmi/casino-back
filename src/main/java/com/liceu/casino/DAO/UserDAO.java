package com.liceu.casino.DAO;

import com.liceu.casino.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
@Transactional
public interface UserDAO extends JpaRepository <User, String> {
    List<User> findByEmail(String email);
    List<User> findByDni(String dni);

    //modificar usuario
    @Modifying
    @Query("update User u set u.name = :name, u.email = :email, u.surname1 = :surname1, " +
            "u.surname2 = :surname2, u.dni = :dni, u.birthDate = :birthDate where u.id = :id")
    void updateProfile(
            @Param("id") long id,
            @Param("name") String name,
            @Param("email") String email,
            @Param("surname1") String surname1,
            @Param("surname2") String surname2,
            @Param("dni") String dni,
            @Param("birthDate") String birthDate
    );

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    void updatePass(@Param("id") long id, @Param("password") String password);

    boolean existsByEmailAndPassword(String email, String encode);
    @Query("SELECT u.coins FROM User u WHERE u.id = :userId")
    Long getUserCoins(Long userId);
    @Modifying
    @Query("update User u set u.coins = :currentCoins where u.id = :id")
    void saveCoinsByUserId(@Param("currentCoins") Long currentCoins, @Param("id") long id);
}
