package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userService {
    @Autowired
    UserDAO userdao;
    public boolean signup(RegisterForm registerForm){
        //existe un usuario con ese nombre?
        if (!userdao.findByEmail(registerForm.getEmail()).isEmpty()) return false;
        //crea e inserta el usuario
        User user = new User(
                registerForm.getName(),
                registerForm.getEmail(),
                //encriptar contraseña
                registerForm.getPassword()
        );
        userdao.save(user);
        return true;
    }

}
