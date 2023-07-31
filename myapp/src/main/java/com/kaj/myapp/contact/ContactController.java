package com.kaj.myapp.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/contacts")
public class ContactController {

//    Map<String, Contact> map = new ConcurrentHashMap<>();

    //@Autowired : Bean 객체를 의존성 주입
    //Bean 객체: @Configuration 클래스에 등록된 싱글턴 클래스로 생성된 객체

    // 싱글턴: 첫 실행시점 객체가 1번 생성됨. 이후부터는 생성된 객체를 재사용
    // static: JVM 실생 시점에 객체를 생성

    //@Configuration 클래스에 Bean 객체를 선언
    // 메서드의 반환타입이 싱글턴 클래스


    //스프링 프레임워크의 DATA JPA 라이브러리
    //@Repository 인터페이스에 맞는 동적 클래스를 실행 시점에 생성하여
    // --- Hibernate 프레임워크로 구성된 클래스(insert, update...) 생성
    // @Autowired 키워드가 있는 선언 변수에 주입한다.

    //의존성 주입(Dependency Injection)
    //객체를 사용하는 곳 외부에서 객체를 생성한 후에
    //객체를 사용하는 곳에 필드, 메서드 매개변수로 넣어줌

    //스프링프레임워크에서는
    // 1. @Configuration 클래스의 @Bean 클래스를 생성
    //    -> @Bean 클래스: 싱글턴 객체를 생성하는 메서드의 반환 타입(클래스)
    // 2. @Autowired 어노테이션 변수에 객체를 의존성 주입


    /*
 -----객체 생성을 정의하는 곳
@Configuration
public class ResourceConfig {
    @Bean
    //메서드의 리턴타입의 클래스 의존성 주입을 할 수 있게 됨.
    public Repository getRepo() {
        return new Repository();   //이 생성자의 정보가 바뀔 수 있기 때문에 이 방법을 사용함.
				Config con = new Config();
				return new Repository(con);
				이런식으로 변경 되면 아래의 생성자들도 모두 바꿔야 하기 때문에
    }
}
-----------------------------------------객체를 사용하는 곳(생성구문 사용X)
public class A{
  @Autowired
  Repository repo;
}
//여기서 한번 만들고


//아래서 재활용
public class B{
  @Autowired
  Repository repo;
}
     */
    //외부에서 객체를 생성해서 넣는 것. ↓
    @Autowired
    ContactRepository repo;

    //생성자 방식으로 넣어주는 방법 ↓ 테스트 할 때 하기도 함.
//    @Autowired
//    public ContactController(ContactRepository repo) {
//        this.repo = repo;
//    }

    @GetMapping
    public List<Contact> getContactList() {
        //repo.findAll(); 전체 테이블 목록 조회
        // SELECT * FROM 테이블
        // repo.findAll(Sort sort); 정렬하여 전체 테이블 목록 조회
        // SELECT * FROM 테이블 ORDER BY 정렬컬럼, 정렬컬럼.....

        //JPA Repository Sort 인터페이스 사용
//        List<Contact> list = repo.findAll(Sort.by("name").ascending());

        //Native-Query를 이용한 방법
        List<Contact> list = repo.getContactSortByName();
        return list;
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> addContact(@RequestBody Contact contact) {

        //클라이언트에서 넘어온 JSON이 객체로 잘 변환됐는지 확인
        System.out.println(contact.getName());
        System.out.println(contact.getPhone());
        System.out.println(contact.getEmail());

        //이메일 필수값 검증
        //400: bad request
        if(contact.getEmail() == null || contact.getEmail().isEmpty()){
            //응답 객체 생성
            Map<String, Object> res = new HashMap<>();
            res.put("data", null);
            res.put("message", "[email] is Required Field");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        //이메일(key) 형식 검증
        //400: bad request

        //이메일(key) 중복 검증
        //409: conflict

        if(contact.getEmail() != null && repo.findById(contact.getEmail()).isPresent()) {
            // 맵에 해당 이메일이 있음
            // 이미 있는 데이터를 클라이언트(브라우저) 보냈거나
            // 클라이언트에서 중복 데이터를 보냈거나..
            Map<String, Object> res = new HashMap<>();
            res.put("data", null);
            res.put("message", "동일한 정보가 이미 존재합니다.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        //에러메세지를 자세하게 하면 취약해질 수 있어서, 400번으로 통일하기도 함.

        //맵에 객체 추가
//        map.put(contact.getEmail(), contact);

        //테이블에 레코드 추가
        // key값이 테이블에 이미 있으면 update
        // 없으면 insert 구문이 실행됨.
        repo.save(contact);

        // contact 테이블에 추가
        // 해당 테이블에 현재 key가 동일한 것이 있으면 update(수정)
        // key가 동일한 것이 없으면 insert(추가)

        //응답 객체 생성
        //실제로 생성된 객체를 응답
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", map.get(contact.getEmail()));
//        res.put("message", "created");


        //응답 객체 생성(ResponseEntity)
        //상태코드, 데이터, 메세지
        // 실제로 생성된 레코드(row)를 응답

        //repo.findById(PK값); 이걸로 실제로 생성된 값을 가져올 수 있음.
        //Optional은 null이 될 수 없음.
        Optional<Contact> savedContact = repo.findById(contact.getEmail());

        //레코드가 존재하는지 여부
        if(savedContact.isPresent()){
            Map<String, Object> res = new HashMap<>();
            res.put("data", repo.findById(contact.getEmail()));
            res.put("message", "created");

            //HTTP Status Code: 201 Created
            // 리소스가 정상적으로 생성되었음.
            return  ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

        return  ResponseEntity.ok().build();
    }

    /* DELETE /contacts/{email}
	        :Path(경로)Variable(변수)
	   DELETE /contacts/kdkcom@naver.com
	*/

    @DeleteMapping(value = "/{email}")
    //@PathVariable("email")
    //경로 문자열{email}과 변수명 String email이 동일하면 안 써도 된다.
    public ResponseEntity removeContact(@PathVariable String email) {
        System.out.println(email);

        // 해당 키(key)의 데이터가 없으면
//        if(map.get(email) == null) {
        //PK값으로 레코드로 1건 조회해서 없으면
        if(!(repo.findById(email).isPresent())){
            //404: NOT FOUND, 해당 경로에 리소스가 없다
            //  DELETE /contacts/kdk@naver.com
            // Response Status Code : 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
//        //객체(리소스-서버의 램) 삭제
//        map.remove(email);
        //레코드(리소스-데이터베이스의 파일의 일부분) 삭제
        repo.deleteById(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
