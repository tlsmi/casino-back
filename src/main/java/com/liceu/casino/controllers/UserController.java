package com.liceu.casino.controllers;

import com.liceu.casino.DTO.ProfileDTO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.PasswordForm;
import com.liceu.casino.forms.ProfileForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import com.liceu.casino.services.TokenService;
import com.liceu.casino.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public Map<String , String> signup(@RequestBody ProfileForm profileForm, HttpServletResponse response){
        Map<String, String> map = new HashMap<>();
        if (!userService.validateFormData(profileForm)) {
            map.put("message","No te pases de listo");
            response.setStatus(400);
            return map;
        }
        if (userService.signup(profileForm)) map.put("message", "Usuario creado correctamente");
        else{
            map.put("message", "Ya existe un usuario con ese correo");
            response.setStatus(400);
        }
        return map;
    }

    @PutMapping("/profile")
    @CrossOrigin
    public Map<String, Object> updateProfile(HttpServletRequest request, HttpServletResponse response,
                                             @RequestHeader("Authorization") String token,
                                             @RequestBody ProfileForm profileForm){
        Map<String, Object> map = new HashMap<>();
        String email = tokenService.getEmail(token.replace("Bearer ",""));
        User user = userService.getUserByEmail(email);
        if (!userService.validatePassword(user.getPassword(), profileForm.getPassword())){
            map.put("message", "Contraseña incorrecta");
            response.setStatus(400);
            return map;
        }
        if(user == null){
            map.put("message", "No existe un usuario con ese correo");
            response.setStatus(400);
            return map;
        }
        if(!userService.validateEmail(profileForm, user.getEmail())){
            map.put("message", "Ya existe un usuario con ese correo");
            response.setStatus(400);
            return map;
        }
        userService.updateProfile(user.getId(), profileForm);
        //actualizar token
        token = tokenService.newToken(profileForm.getEmail());

        map.put("token", token);
        map.put("message", "Usuario modificado correctamente");

        return map;
    }

    @PutMapping("/profile/password")
    @CrossOrigin
    public Map<String, Object> updatePassword(HttpServletRequest request, HttpServletResponse response,
                                             @RequestHeader("Authorization") String token,
                                             @RequestBody PasswordForm passwordForm){

        User user = userService.getUserByEmail(tokenService.getEmail(token.replace("Bearer ","")));

        boolean oldPassValidation = userService.validate(user.getEmail(), passwordForm.getCurrentPassword());
        Map<String, Object> map = new HashMap<>();

        if (passwordForm.getNewPassword().equals(passwordForm.getCurrentPassword())){
            map.put("message", "Tu contraseña es igual que la anterior");
            response.setStatus(400);
        }else if (oldPassValidation){
            userService.changePass(user, passwordForm.getNewPassword());
            map.put("message", "Contraseña cambiada correctamente");
        }else {
            map.put("message", "contraseña actual incorrecta");
            response.setStatus(403);
        }

        return map;

    }


    @GetMapping("/getprofile")
    @CrossOrigin
    public Object getprofile(@RequestHeader("Authorization") String token){
        User user = userService.getUserByEmail(tokenService.getEmail(token.replace("Bearer ","")));
        ProfileDTO profile = userService.newProfile(user);
        return profile;
    }



}
