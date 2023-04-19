package com.liceu.casino.controllers;

import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import com.liceu.casino.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @CrossOrigin
    public String login(@RequestBody LoginForm loginForm, HttpServletResponse response){
        User user = userService.login(loginForm);
        if(user == null) {
            System.out.println("Credenciales de inicio de sesión incorrectos");
            return null;
        }
        System.out.println("Sesión iniciada correctamente");

        //token a partir del usuario encontrado
        String token = "token";

        return null;
    }

    @PostMapping("/signup")
    @CrossOrigin
    public String signup(@RequestBody RegisterForm registerForm, HttpServletResponse response){
        if (userService.signup(registerForm)) System.out.println("Usuario creado correctamente");
        else System.out.println("El usuario ya existe o las contraseñas no coinciden");
        return null;
    }

}
