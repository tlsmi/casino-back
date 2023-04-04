package com.liceu.casino.controllers;

import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @PostMapping("/login")
    public String login(@RequestBody LoginForm loginForm, HttpServletResponse response){
        return null;
    }

    @PostMapping("/signup")
    @CrossOrigin
    public String signup(@RequestBody RegisterForm registerForm, HttpServletResponse response){
       return null;
    }

}
