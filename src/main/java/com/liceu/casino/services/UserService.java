package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userdao;
    public Object[] signup(RegisterForm registerForm, HttpServletResponse response){
        System.out.println(registerForm);
        // Dependiendo del error que da, guarda este error en el array de Objetos y lo envía al controlador
        if (!isOlder(registerForm)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("EDAD ERROR!");
            return new Object[]{false, "El usuario debe tener al menos 18 años para registrarse!"};
        } else if (existEmail(registerForm.getEmail())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("EMAIL ERROR!");
            return new Object[]{false, "Este email ya esta registrado!"};
        } else if (existDni(registerForm.getDni())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("DNI ERROR!");
            return new Object[]{false, "Este DNI ya esta registrado!"};
        }
        //crea e inserta el usuario
        User user = new User(
                registerForm.getDni(),
                registerForm.getName(),
                registerForm.getSurname1(),
                registerForm.getSurname2(),
                registerForm.getEmail(),
                registerForm.getBirthDate(),
                registerForm.getGender(),
                //encriptar contraseña
                registerForm.getPassword()
        );
        System.out.println(user);
        userdao.save(user);
        return new Object[]{true, "Usuario creado correctamente!"};
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
        if (u.getPassword().equals(loginForm.getPassword())) return u;
        return null;
    }

    public boolean existEmail(String email) {
        return userdao.findByEmail(email).size() > 0;
    }

    public boolean existDni(String dni) {
        return userdao.findByDni(dni).size() > 0;
    }
}
