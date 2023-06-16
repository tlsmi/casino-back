package com.liceu.casino.controllers;

import com.liceu.casino.DTO.ProfileDTO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.PasswordForm;
import com.liceu.casino.forms.ProfileForm;
import com.liceu.casino.model.User;
import com.liceu.casino.services.TokenService;
import com.liceu.casino.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.login(loginForm);
        if (user == null) {
            map.put("message", "Credenciales de inicio de sesión incorrectos");
            response.setStatus(400);
            return map;
        }

        // Crear perfil
        ProfileDTO profile = userService.newProfile(user);

        // Token a partir del usuario encontrado
        String token = tokenService.newToken(user.getEmail());

        map.put("token", token);
        map.put("user", profile);
        return map;
    }

    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody ProfileForm profileForm, HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        String message = userService.validateFormData(profileForm);
        if (!message.equals("Usuario creado correctamente")) {
            response.setStatus(400);
        }
        map.put("message", message);
        return map;
    }

    @PutMapping("/profile")
    public Map<String, Object> updateProfile(HttpServletResponse response,
                                             @RequestHeader("Authorization") String token,
                                             @RequestBody ProfileForm profileForm) {
        Map<String, Object> map = new HashMap<>();
        String email = tokenService.getEmail(token.replace("Bearer ", ""));
        User user = userService.findByEmail(email);
        if (user == null) {
            map.put("message", "No existe un usuario con ese correo");
            response.setStatus(400);
            return map;
        }
        if (!userService.validatePassword(user.getPassword(), profileForm.getPassword())) {
            map.put("message", "Contraseña incorrecta");
            response.setStatus(400);
            return map;
        }
        if (!userService.validateEmail(profileForm, user.getEmail())) {
            map.put("message", "Ya existe un usuario con ese correo");
            response.setStatus(400);
            return map;
        }
        userService.updateProfile(user.getId(), profileForm);
        // Actualizar token
        token = tokenService.newToken(profileForm.getEmail());

        map.put("token", token);
        map.put("message", "Usuario modificado correctamente");

        return map;
    }

    @PutMapping("/profile/password")
    public Map<String, Object> updatePassword(HttpServletRequest request, HttpServletResponse response,
                                              @RequestHeader("Authorization") String token,
                                              @RequestBody PasswordForm passwordForm) {

        User user = userService.findByEmail(tokenService.getEmail(token.replace("Bearer ", "")));

        boolean oldPassValidation = userService.validate(user.getEmail(), passwordForm.getCurrentPassword());
        Map<String, Object> map = new HashMap<>();

        if (passwordForm.getNewPassword().equals(passwordForm.getCurrentPassword())) {
            map.put("message", "Tu contraseña es igual que la anterior");
            response.setStatus(400);
        } else if (oldPassValidation) {
            userService.changePass(user, passwordForm.getNewPassword());
            map.put("message", "Contraseña cambiada correctamente");
        } else {
            map.put("message", "contraseña actual incorrecta");
            response.setStatus(403);
        }

        return map;

    }


    @GetMapping("/getprofile")
    public Object getprofile(@RequestHeader("Authorization") String token) {
        User user = userService.findByEmail(tokenService.getEmail(token.replace("Bearer ", "")));
        ProfileDTO profile = userService.newProfile(user);
        return profile;
    }

    @GetMapping("/credito")
    public Map<String, Object> getCredito(@RequestHeader("Authorization") String token) {
        User user = userService.findByEmail(tokenService.getEmail(token));
        Map<String, Object> map = new HashMap<>();
        map.put("credito", user.getCoins());
        return map;
    }

    @DeleteMapping("/deleteUser")
    public Map<String, String> updatePassword(HttpServletResponse response, @RequestBody LoginForm loginForm , @RequestHeader("Authorization") String token) {

        Map<String, String> map = new HashMap<>();
        System.out.println("Token1: " + token);
        System.out.println("Token2: " + token.replace("Bearer ", ""));
        User userLogged = userService.findByEmail(tokenService.getEmail(token.replace("Bearer ", "")));

        User user = userService.login(loginForm);
        if (user == null) {
            System.out.println("***Credenciales de inicio de sesión incorrectos***");
            map.put("message", "Credenciales de inicio de sesión incorrectos");
            response.setStatus(400);
            return map;
        }
        if (userLogged.getEmail().equals(user.getEmail())) {
            userService.deleteUser(userLogged);
            map.put("message","ok");
        } else {
            map.put("message", "Error al borrar usuario, Discrepancia entre emails");
        }
        return map;
    }
}