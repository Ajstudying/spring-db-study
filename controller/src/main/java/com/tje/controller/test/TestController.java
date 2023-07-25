package com.tje.controller.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@Controller
// = @Component 어노테이션의 일종
//@ResponseBody

@RestController
public class TestController {

    //http://localhost:8080/hello
    //localhost(본인 컴퓨터), 8080 포트 번호 접속 -> 프로그램이 돌고 있어야 됨. 아니면 연결이 안됨.
    //http://localhost:8080 이것만 하면 연결 안됨 뜸.

    // /hello, 경로(path)에 대한 요청(request)에 대한 응답(response)
    //HTTP method: GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD ...
    //주소창에 주소를 입력하고 엔터 요청에 대한 응답이 나옴
    //GET(조회) http://localhost:8080/hello (보통 get요청이라는 건 조회를 의미함.)

    //Dispatcher Servlet이 요청 경로에 맞는 메서드를 호출함.
    // Dispatcher(디스패처): 중간에 요청에서 분기를 해주고 응답을 통하여 처리
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello() {
        return "Hello, spring framework!";
    }
}
