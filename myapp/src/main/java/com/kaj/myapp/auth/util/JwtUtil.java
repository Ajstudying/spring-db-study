package com.kaj.myapp.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kaj.myapp.auth.AuthProfile;
import com.kaj.myapp.auth.entity.Profile;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

public class JwtUtil {

    //임의의 서명 값 (로그인시 같은 값을 넣더라도, 해당 코드가 입력이 안되면 안되는.. 관리자 코드 같은것..?)
    // - 키파일 등을 읽어서 처리 가능
    public String secret = "youre-secret";
    //ms 단위
    public final long TOKEN_TIMEOUT = 1000 * 60 * 60 * 24 * 7;

    //JWT 토큰 생성
    public String createToken(Long id, String username, String nickname){
        //토큰 생성시간과 만료시간을 만듬
        Date now = new Date();
        // 만료시간: 2차인증 이런게 잘 걸려 있으면 큰 문제는 안됨. 내 컴퓨터를 다른 사람이 쓴다.
        //길게: 7일 ~ 30일
        //보통: 1시간 ~ 3시간
        //짧게: 5분 ~ 15분
        Date exp = new Date(now.getTime() + TOKEN_TIMEOUT);

        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                //sub: 토큰 소유자
                .withSubject(id.toString())
                .withClaim("username", username)
                .withClaim("nickname", nickname)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(algorithm);

    }

    public AuthProfile validateToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 검증 객체 생성
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            //토큰 검증이 제대로 된 상황
            //토큰 페이로드(데이터, subject/claim)을 조회
            Long id = Long.valueOf(decodedJWT.getSubject());
            String nickname = decodedJWT.getClaim("nickname").asString();
            String username = decodedJWT.getClaim("username").asString();

            return AuthProfile.builder().id(id).username(username).nickname(nickname).build();
        } catch (JWTVerificationException e){
            //토큰 검증 오류 상황
            return null;
        }


    }

}
