package com.kaj.myapp.auth;


import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashUtil {
    public String createHash(String cipherText) {

        // 'hashToString' salt와 함께 해시를 생성
        return BCrypt.withDefaults().hashToString(12, cipherText.toCharArray());
    }
}
