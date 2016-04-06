package edu.cs.dartmouth.cs165.myruns.vishal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vishal Gaurav
 */
public class Validator {
    public static final int PASSWORD_LENGTH = 8;
    public static final int NAME_LENGTH = 1 ;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidLength(String password, int length){
        return password!=null && !password.isEmpty() && password.length() >= length ;
    }
}
