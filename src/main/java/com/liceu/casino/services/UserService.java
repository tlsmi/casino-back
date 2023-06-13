package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import com.liceu.casino.utils.SHA512Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userdao;
    SHA512Encoder encoder;
    public boolean signup(RegisterForm registerForm){
        System.out.println(registerForm);
        //existe un usuario con ese nombre o las contraseñas no coinciden?
        if (userdao.findByEmail(registerForm.getEmail()) != null) return false;
        //crea y guarda el usuario
        User user = new User(
                registerForm.getDni(),
                registerForm.getName(),
                registerForm.getSurname1(),
                registerForm.getSurname2(),
                registerForm.getEmail(),
                registerForm.getBirthDate(),
                //encriptar contraseña
                encoder.encode(registerForm.getPassword())
        );
        System.out.println(user);
        userdao.save(user);
        return true;
    }

    public User login(LoginForm loginForm) {
        //si no encuentra usuario con ese email peta
        if (userdao.findByEmail(loginForm.getEmail()) == null) return null;

        //crea usuario asociado a ese mail
        User u = userdao.findByEmail(loginForm.getEmail());

        //falta comprobar la contraseña encriptada, de momento está sin encriptar
        //si coincide la contraseña del usuario encontrado con la introducida (ambas encriptadas) lo devuelve
        if (u.getPassword().equals(encoder.encode(loginForm.getPassword()))) return u;
        return null;
    }

    public User findByEmail(String email) {
        return userdao.findByEmail(email);
    }

    public void setCredito(long credito, User user) {
        userdao.updateUser(credito, user.getEmail());
    }
}