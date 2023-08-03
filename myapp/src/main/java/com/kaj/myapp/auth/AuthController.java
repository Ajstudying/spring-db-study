package com.kaj.myapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginRepository repo;
    @Autowired
    private AuthService service;

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



}
