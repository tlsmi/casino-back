package com.liceu.casino.controllers;

import com.liceu.casino.DTO.ProfileDTO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import com.liceu.casino.services.TokenService;
import com.liceu.casino.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    @CrossOrigin
    public Map<String, Object> login(@RequestBody LoginForm loginForm, HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        User user = userService.login(loginForm);
        if(user == null) {
            System.out.println("***Credenciales de inicio de sesión incorrectos***");
            map.put("message", "Credenciales de inicio de sesión incorrectos");
            response.setStatus(400);
            return map;
        }
        System.out.println("***Sesión iniciada correctamente***");

        //crear perfil
        ProfileDTO profile = userService.newProfile(user);

        //token a partir del usuario encontrado
        String token = tokenService.newToken(user.getEmail());

        System.out.println("Esto es el token: " + token);
        System.out.println("Esto deberia ser el correo " + tokenService.getEmail(token));

        map.put("token", token);
        map.put("user", profile);
        return map;
    }

    @PostMapping("/signup")
    @CrossOrigin
    public Map<String , String> signup(@RequestBody RegisterForm registerForm, HttpServletResponse response){
        Map<String, String> map = new HashMap<>();
        if (userService.signup(registerForm)) map.put("message", "Usuario creado correctamente");
        else{
            map.put("message", "Ya existe un usuario con ese correo");
            response.setStatus(400);
        }
        return map;
    }

}
