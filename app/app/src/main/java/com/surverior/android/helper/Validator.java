package com.surverior.android.helper;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Azhar Fauzan Dz on 4/14/2016.
 */
public class Validator {

    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z\\s]{2,100}$";
    private static Pattern patternEmail = Pattern.compile(EMAIL_REGEX);
    private static Pattern patternName = Pattern.compile(USERNAME_REGEX);

    public static boolean isValidEmail(String email){
        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String pass){
        return pass.length() >= 8;
    }

    public static boolean isValidName (String name){

        if (name.length() < 2)
            return false;

        Matcher matcher = patternName.matcher(name);
        return matcher.matches();
    }
}
