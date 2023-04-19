package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userdao;
    public boolean signup(RegisterForm registerForm){
        System.out.println(registerForm);
        //existe un usuario con ese nombre o las contraseñas no coinciden?
        if (!userdao.findByEmail(registerForm.getEmail()).isEmpty()) return false;
        if (!registerForm.getPassword().equals(registerForm.getPasswordRep())) return false;
        //crea e inserta el usuario
        User user = new User(
                registerForm.getName(),
                registerForm.getEmail(),
                //encriptar contraseña
                registerForm.getPassword()
        );
        System.out.println(user);
        userdao.save(user);
        return true;
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
}
