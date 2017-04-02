package com.dumindudulanga.cwb;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Created by Dulanga on 4/2/2017.
 */

public class User {
    public String username;
    public String email;
    public String password;
    private static MessageDigest md;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String password) {
        this.username = username;
        this.email = email;
        this.password = cryptWithMD5(password);
    }

    public static String cryptWithMD5(String pass){
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Log.e("NoSuchAlgo",ex.toString());
        }
        return null;


    }
}
