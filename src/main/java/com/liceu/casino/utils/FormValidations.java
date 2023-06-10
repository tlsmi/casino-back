package com.liceu.casino.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidations {

    public static boolean validateDNI(String dni){
        Pattern pat = Pattern.compile("^\\d{8}[a-zA-Z]$");
        Matcher mat = pat.matcher(dni);
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean validatePassword(String pass){
        if(pass.length() >= 8) return true;
        return false;
    }
    public static boolean validateEmail(String email){
        Pattern pat = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher mat = pat.matcher(email);
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean validateBirthDate(String birthDate){
        Pattern pat = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        Matcher mat = pat.matcher(birthDate);
        Date today = new Date();
        Date eighteenYearsAgo = new Date(today.getYear() - 18, today.getMonth(), today.getDate());
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }

}
