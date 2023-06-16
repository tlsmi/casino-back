package com.liceu.casino.utils;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormValidations {

    @Autowired
    UserDAO userDAO;

    public boolean repetidoDNI(String dni) {
        User user = userDAO.findByDni(dni);
        return user != null;
    }

}
