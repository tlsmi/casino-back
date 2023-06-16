package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.DTO.ProfileDTO;
import com.liceu.casino.forms.LoginForm;
import com.liceu.casino.forms.ProfileForm;
import com.liceu.casino.model.User;
import com.liceu.casino.utils.FormValidations;
import com.liceu.casino.utils.SHA512Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userdao;

    @Autowired
    SHA512Encoder encoder;

    @Autowired
    FormValidations validations;

    public boolean signup(ProfileForm registerForm) {
        // Si no existe un usuario con este email ...
        if (userdao.findByEmail(registerForm.getEmail()) != null) return false;
        // Crea y guarda el usuario
        User user = new User(
                registerForm.getDni(),
                registerForm.getName(),
                registerForm.getSurname1(),
                registerForm.getSurname2(),
                registerForm.getEmail(),
                registerForm.getBirthDate(),
                // Encriptar contraseña
                encoder.encode(registerForm.getPassword())
        );
        userdao.save(user);
        return true;
    }

    public User login(LoginForm loginForm) {
        // Si no encuentra usuario con ese email ...
        if (userdao.findByEmail(loginForm.getEmail()) == null) return null;

        // Crea un usuario asociado a el email enviado
        User u = userdao.findByEmail(loginForm.getEmail());

        // Si coincide la contraseña del usuario encontrado con la introducida (ambas encriptadas) ...
        if (u.getPassword().equals(encoder.encode(loginForm.getPassword()))) return u;
        return null;
    }

    public User findByEmail(String email) {
        return userdao.findByEmail(email);
    }

    public void setCredito(long credito, User user) {
        userdao.updateUser(credito, user.getEmail());
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

    public boolean validateEmail(ProfileForm profileForm, String oldEmail) {
        return userdao.findByEmail(profileForm.getEmail()) == null || profileForm.getEmail().equals(oldEmail);
    }

    public boolean validatePassword(String currentPass, String pass) {
        return currentPass.equals(encoder.encode(pass));
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

    public String validateFormData(ProfileForm registerForm) {
        if (!validations.repetidoDNI(registerForm.getDni())) {
            if (signup(registerForm)) {
                return "Usuario creado correctamente";
            } else {
                return "El email ya esta registrado";
            }
        } else {
            return "El DNI ya existe";
        }
    }

    public void deleteUser(User user) {
        userdao.deleteUser(user.getId());
    }

    public void buyCoins(User user, int coinsInt) {
        Long currentCoins = userdao.getUserCoins(user.getId());
        System.out.println("monedas actuales" + currentCoins);
        currentCoins = currentCoins += coinsInt;
        userdao.saveCoinsByUserId(currentCoins, user.getId());
    }
}
