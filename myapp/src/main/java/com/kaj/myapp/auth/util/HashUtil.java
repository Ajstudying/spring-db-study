package com.kaj.myapp.auth.util;


import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashUtil {
    public String createHash(String cipherText) {
        //Bcrypt Hash 생성

        // https://www.baeldung.com/java-password-hashing
        // https://mia-dahae.tistory.com/120

        // https://blog.kakaocdn.net/dn/IdglW/btrEoJ6HDUJ/FmJqCChB9NCXd6fapmJdAk/img.png
        // 'hashToString': Salt와 함께 해시를 생성
        // hash create할 때는 salt 랜덤으로 생성해서 저장
        return BCrypt.withDefaults().hashToString(12, cipherText.toCharArray());
    }

    //문자열을 받아서 salt와 함께 hash가 맞는지 확인
    // ciphertext(암호화 안된 문자열), plaintext(구조가 없는 문자열)

    //hash: $버전$라운드횟수$22바이트salt+해시문자열
    public boolean verifyHash(String cipherText, String hash){
        //hash를 verifying 할 때는 이미 있는 salt값으로
        // ciphertext를 결합하여 hash와 맞는지 확인

        return BCrypt
                .verifyer()
                .verify(cipherText.toCharArray(), hash)
                .verified;
    }

}
