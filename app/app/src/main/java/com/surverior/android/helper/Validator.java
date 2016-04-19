package com.surverior.android.helper;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Azhar Fauzan Dz on 4/14/2016.
 */
public class Validator {

    private static final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static Pattern pattern = Pattern.compile(regex);

    public static boolean isValidEmail(String email){
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String pass){
        return pass.length() >= 8;
    }
}
