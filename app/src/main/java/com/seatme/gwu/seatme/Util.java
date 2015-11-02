package com.seatme.gwu.seatme;

/**
 * Created by Huanzhou on 2015/11/2.
 */
public class Util {

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


}
