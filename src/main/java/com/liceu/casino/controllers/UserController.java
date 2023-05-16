package com.liceu.casino.controllers;

import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import com.liceu.casino.services.TokenService;
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
    @Autowired
    TokenService tokenService;

    // Como no tenemos un front por ahora, he hecho un array de Objetos para imprimir el resultado en pantalla del Postman
    // Tambien lo podeis ver en la consola de Spring
    @PostMapping("/signup")
    @CrossOrigin
    public Object signup(@RequestBody RegisterForm registerForm, HttpServletResponse response){
        System.out.println(registerForm.toString());
        Map<String, Object> map = new HashMap<>();
        // Guarda el booleano y la respuesta correspondiente para luego imprimirla por pantalla
        Map<String, Object> respuesta = userService.signup(registerForm, response);
        if ((Boolean) respuesta.get("boolean")) {
            System.out.println(respuesta.get("message"));
            map.put("message", respuesta.get("user"));
            return map;
        }
        else System.out.println(respuesta.get("message"));
        return respuesta.get("message");
    }

    @PostMapping("/login")
    @CrossOrigin
    public Map<String, Object> login(@RequestBody LoginForm loginForm, HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        User user = userService.login(loginForm);
        if(user == null) {
            System.out.println("Credenciales de inicio de sesión incorrectos");
            return null;
        } else {
            System.out.println("Sesión iniciada correctamente");
            String token = tokenService.newToken(user.getEmail());
             map.put("token", token);
             map.put("user", userService.createUserMap(user));
        }
        return map;
    }

    @PostMapping("/hola")
    @CrossOrigin
    public Map<String, Object> hola(){
        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "HOLA");
        return map;
    }
}
