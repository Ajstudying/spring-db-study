package com.kaj.myapp.auth;

import com.kaj.myapp.auth.configuration.AuthConfiguration;
import com.kaj.myapp.auth.entity.Login;
import com.kaj.myapp.auth.entity.LoginRepository;
import com.kaj.myapp.auth.entity.Profile;
import com.kaj.myapp.auth.entity.ProfileRepository;
import com.kaj.myapp.auth.request.SignupRequest;
import com.kaj.myapp.auth.util.HashUtil;
import com.kaj.myapp.auth.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginRepository repo;

    @Autowired
    private ProfileRepository profileRepo;

    @Autowired
    private AuthService service;

    //AuthController 와 HashUtil은 중간수준의 결합도(coupling)
    //HashUtil 객체를 메서드에서 생성 높은 수준의 결합도(coupling)
    @Autowired
    private HashUtil hash;
    //HashUtil과 JwtUtil은 AuthConfiguration에 @Configuration으로 싱글턴객체 생성을 해놨기 때문에
    // @Autowired로 해당 객체를 가져와서 사용할 수 있다.

    @Autowired
    private JwtUtil jwt;

//    @GetMapping(value = "/logins")
//    public List<Login> getLogins(){
//
//        return repo.findAll();
//    }




    //profile 정보 생성과 insert 정보 생성은 1개의 처리로 실행
    // 둘중에 하나라도 FAIL(오류)가 나면 둘다 생성이 안되어야 함.

    //insert, update, delete DML(데이터 조작)
    //데이터 조작작업에서 논리적으로 1개 묶을 수 있는 방법이 트랜잭션
    //트랜잭션(transaction): 거래
    //Controller에서는 transaction 처리가 안 됨. 그래서 서비스 생성

        /*
        @Controller: 요청값 검증, 간단한 데이터 조작, 적절한 응답값 반환
        @Service: 트랜잭션처리, 외부시스템 연동
         */

    //profile 정보 생성: insert

    //OK
    //login 정보 생성(profile_id를 넣어서 생성): insert
    //FAIL -> 서버에러 500, 프로필만 생성되고, 로그인 정보 없이 생성된 프로필이 붕 떠버림.
    @PostMapping(value = "/signup")
    public ResponseEntity siginUp(@RequestBody SignupRequest req) {
        System.out.println(req);
        //1.Validation
        //입력값 검증
        // 이름 없거나, 패스워드 없거나, 닉네임, 이메일 없음..
        if(req.getUsername() == null || req.getUsername().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getPassword() == null || req.getPassword().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getEmail() == null || req.getEmail().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getNickname().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        //2. Buisiness Logic(데이터 처리)
        //profile, login 생성 트랜젝션 처리
        long profileId = service.createIdentity(req);

        //3. Response
        //201:created
        return ResponseEntity.status(HttpStatus.CREATED).body(profileId);
    }
    /*
    1.(브라우저)(로그인 요청)
    [RequestLine]
    HTTP 1.1 POST 로그인 주소
    [RequestHeader]
    content-type: www-form-urlencoded
    [Body]
    id=...&pw=...

    2.(서버) 로그인 요청을 받고 인증처리 후 쿠키 응답 및 웹페이지로 이동
    HTTP Status 302(리다이렉트)
    [Response Header]
    Set-Cookie: 인증키 =키......; domain=.naver.com
    Location: "리다이렉트 주소"

    3.(브라우저) 쿠키를 생성(도메인에 맞게)

    */
    //GET 주소에 해당값이 붙어서 옴, PUT 값이 body에 붙어서 옴.
    @PostMapping(value = "/signin")
    public ResponseEntity signIn(@RequestParam String username, @RequestParam String password, HttpServletResponse res){
        System.out.println(username);
        System.out.println(password);
        //1. username, pw 인증확인
        //   1.1 username으로 login 테이블에서 조회 후 id, secret까지 조회.
        Optional<Login> login = repo.findByUsername(username);
        if(!login.isPresent()){
            //401 Unauthorized
            //클라이언트에서는 대충 뭉뚱그려서 [인증정보가 잘못되었습니다.]
            //[사용자이름 또는 패스워드가 잘못되었습니다.] X
            // 아이디가 잘못되었다, 패스워드가 잘못되었다, 둘다 잘못되었다.
            // 이런식으로 자세하게 response하면 안됨. 유추하기 쉽기 때문에 빌드로 내보냄.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //   1.2 password+salt -> 해시 -> secret 일치 여부 확인
        boolean isVerified = hash.verifyHash(password, login.get().getSecret());
//        System.out.println("verified: "+isVerified);
        //   1.3 일치하면 다음 코드를 실행
        //   1.4 일치하지 않으면 401 Unauthorized 반환 후 종료
        if(!isVerified){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Login l = login.get();
        //2. profile 정보를 조회하여 인증키 생성(JWT)
        Optional<Profile> profile = profileRepo.findByLogin_Id(l.getId());
        //로그인 정보와 프로필 정보가 제대로 연결 안됨.
        if(!profile.isPresent()){
            //409 conflict: 데이터 현재 상태가 안 맞음.
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        String token = jwt.createToken(l.getId(), l.getUsername(), profile.get().getNickname());
        System.out.println(token);

        //3. cookie와 헤더를 생성한 후 리다이렉트
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge((int)(jwt.TOKEN_TIMEOUT/1000));
        cookie.setDomain("localhost");// 쿠키를 사용할 수 있는 도메인
        //응답헤더에 쿠키 추가
        res.addCookie(cookie);

        //웹 첫 페이지로 리다이렉트
//        res.setHeader("Location", "http://localhost:5500");
//        res.setStatus(HttpStatus.FOUND.value()); //HTTP 302 Found(리다이렉트)

        return ResponseEntity
                .status(302)
                .location(ServletUriComponentsBuilder
                        .fromHttpUrl("http://localhost:5500")
                        .build().toUri())
                .build();

    }



}
