package com.surverior.android.helper;


import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Azhar Fauzan Dz on 4/14/2016.
 */
public class Validator {

    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z\\s]{2,100}$";
    private static final String PHONE_REGEX= "^[0-9\\-\\+]{9,15}$";
    private static Pattern patternEmail = Pattern.compile(EMAIL_REGEX);
    private static Pattern patternPhone = Pattern.compile(PHONE_REGEX);
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

    public static boolean isValidPhone(String phone){

        Matcher matcher = patternPhone.matcher(phone);
        return matcher.matches();

    }

    public static boolean isValidBirth(String birth){
        String [] split = birth.split("-");
        int currDate = Integer.parseInt(split[2]);
        int currMonth = Integer.parseInt(split[1]);
        int currYear = Integer.parseInt(split[0]);
        Date date = new Date();
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat dateFormat = new SimpleDateFormat("dd");
        int thisDate = Integer.parseInt(dateFormat.format(date));
        int thisMonth = Integer.parseInt(monthFormat.format(date));
        int thisYear = Integer.parseInt(yearFormat.format(date));

        // Log.e("Tes",dateFormat.format(date));

        //for debugging
        Log.e("Year",""+thisYear);
        Log.e("Month",""+thisMonth);
        Log.e("Date",""+thisDate);
        Log.e("CurrY",""+currYear);
        Log.e("CurrM",""+currMonth);
        Log.e("CurrD",""+currDate);

        if(currYear==thisYear){
            if (currMonth>thisMonth){
                return false;
            }else if (currDate >= thisDate){
                return false;
            }
        }else if(currYear>thisYear){
            return false;
        }
        return true;
    }
}
