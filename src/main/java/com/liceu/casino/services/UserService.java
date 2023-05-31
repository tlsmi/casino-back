package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.DTO.ProfileDTO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.ProfileForm;
import com.liceu.casino.forms.RegisterForm;
import com.liceu.casino.model.User;
import com.liceu.casino.utils.SHA512Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userdao;
    SHA512Encoder encoder;
    public boolean signup(RegisterForm registerForm){
        System.out.println(registerForm);
        //existe un usuario con ese nombre o las contraseñas no coinciden?
        if (!userdao.findByEmail(registerForm.getEmail()).isEmpty()) return false;
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
        if (userdao.findByEmail(loginForm.getEmail()).isEmpty()) return null;

        //crea usuario asociado a ese mail
        User u = userdao.findByEmail(loginForm.getEmail()).get(0);

        //falta comprobar la contraseña encriptada, de momento está sin encriptar
        //si coincide la contraseña del usuario encontrado con la introducida (ambas encriptadas) lo devuelve
        if (u.getPassword().equals(encoder.encode(loginForm.getPassword()))) return u;
        return null;
    }

    public ProfileDTO newProfile(User user) {
        return new ProfileDTO(
                user.getDni(),
                user.getName(),
                user.getSurname1(),
                user.getSurname2(),
                user.getEmail(),
                user.getBirthDate(),
                user.getCoins()
        );
    }

    public User getUserByEmail(String email) {
        List<User> users = userdao.findByEmail(email);
        if (users.isEmpty()) return null;
        return users.get(0);
    }

    public boolean validateEmail(ProfileForm profileForm, String oldEmail){
        if (!userdao.findByEmail(profileForm.getEmail()).isEmpty() && !profileForm.getEmail().equals(oldEmail)) return false;
        return true;
    }

    public boolean validatePassword(String currentPass, String pass){
        if (currentPass.equals(encoder.encode(pass)))return true;
        else return false;
    }
    public void updateProfile(Long id, ProfileForm profileForm) {
        userdao.updateProfile(
                id,
                profileForm.getName(),
                profileForm.getEmail(),
                profileForm.getSurname1(),
                profileForm.getSurname2(),
                profileForm.getDni(),
                profileForm.getBirthDate()
        );
    }

    public boolean validate(String email, String currentPassword) {
        return userdao.existsByEmailAndPassword(email, encoder.encode(currentPassword));
    }

    public void changePass(User user, String newPassword) {
        userdao.updatePass(user.getId(), encoder.encode(newPassword));
    }
}
