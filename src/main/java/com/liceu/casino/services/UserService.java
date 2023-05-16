package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserDAO userdao;

    @Value("${token.expiration}")
    int tokenExpiration;

    public static String getSHA512(String input) {
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes(StandardCharsets.UTF_8));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public Map<String, Object> signup(RegisterForm registerForm, HttpServletResponse response){
        System.out.println(registerForm);
        Map<String, Object> map = new HashMap<>();
        // Dependiendo del error que da, guarda este error en el array de Objetos y lo envía al controlador
        if (!isOlder(registerForm)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("EDAD ERROR!");
            map.put("boolean", false);
            map.put("message", "El usuario debe tener al menos 18 años para registrarse!");
            return map;
        } else if (existEmail(registerForm.getEmail())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("EMAIL ERROR!");
            map.put("boolean", false);
            map.put("message", "Este email ya esta registrado!");
            return map;
        } else if (existDni(registerForm.getDni())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("DNI ERROR!");
            map.put("boolean", false);
            map.put("message", "Este DNI ya esta registrado!");
            return map;
        }
        //crea e inserta el usuario
        User user = new User(
                registerForm.getDni(),
                registerForm.getName(),
                registerForm.getSurname1(),
                registerForm.getSurname2(),
                registerForm.getEmail(),
                registerForm.getBirthDate(),
                //encriptar contraseña
                getSHA512(registerForm.getPassword())
        );
        System.out.println(user);
        userdao.save(user);
        map.put("boolean", true);
        map.put("message", "Usuario creado correctamente!");
        map.put("user", user);
        return map;
    }

    // Método que comprueba si es mayor de 18 años
    private boolean isOlder(RegisterForm registerForm) {
        LocalDate birthDate = LocalDate.parse(registerForm.getBirthDate());
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        int age = period.getYears();
        System.out.println(age);
        return age > 18;
    }

    public User login(LoginForm loginForm) {
        //si no encuentra usuario con ese email peta
        if (userdao.findByEmail(loginForm.getEmail()).isEmpty()) return null;
        //crea usuario asociado a ese mail
        User u = userdao.findByEmail(loginForm.getEmail()).get(0);

        //falta comprobar la contraseña encriptada, de momento está sin encriptar
        //si coincide la contraseña del usuario encontrado con la introducida lo devuelve
        if (u.getPassword().equals(getSHA512(loginForm.getPassword()))) return u;
        return null;
    }

    public boolean existEmail(String email) {
        return userdao.findByEmail(email).size() > 0;
    }

    public boolean existDni(String dni) {
        return userdao.findByDni(dni).size() > 0;
    }

    public Map<String, Object> createUserMap(User user) {
        Map<String, Object> mapUser = new HashMap<>();
        mapUser.put("avatarUrl", "");
        mapUser.put("email", user.getEmail());
        mapUser.put("iat", tokenExpiration);
        mapUser.put("id", getSHA512(String.valueOf(user.getDni())).substring(0, 24));
        mapUser.put("name", user.getName() + " " + user.getSurname1() + " " + user.getSurname2());
        mapUser.put("coins", user.getCoins());
        mapUser.put("birthdate", user.getBirthDate());
        return mapUser;
    }

}
