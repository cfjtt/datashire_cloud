package com.eurlanda.edu.tp.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptionUtil {

    public static String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
