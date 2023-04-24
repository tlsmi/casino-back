package com.liceu.casino.controllers;

import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import com.liceu.casino.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    // Como no tenemos un front por ahora, he hecho un array de Objetos para imprimir el resultado en pantalla del Postman
    // Tambien lo podeis ver en la consola de Spring
    @PostMapping("/signup")
    @CrossOrigin
    public String signup(@RequestBody RegisterForm registerForm, HttpServletResponse response){
        System.out.println(registerForm.toString());
        // Guarda el booleano y la respuesta correspondiente para luego imprimirla por pantalla
        Object[] respuesta = userService.signup(registerForm, response);
        if ((Boolean) respuesta[0]) System.out.println(respuesta[1]);
        else System.out.println(respuesta[1]);
        return (String) respuesta[1];
    }

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
}
