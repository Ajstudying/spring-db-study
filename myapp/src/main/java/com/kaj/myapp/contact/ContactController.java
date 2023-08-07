package com.kaj.myapp.contact;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/contacts")
public class ContactController {

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
//        List<Contact> list = repo.findContactsSortByName();

        // repository query creation을 이용한 방법
        List<Contact> list = repo.findAllByOrderByName();

        return list;
    }

    //GET /contacts/paging?page=0&size=10
    //query-string으로 받을 것임
    //?키=값&키=값....
    //@RequestParam
    //query-string 값을 매개변수로 받는 어노테이션
    @Auth
    @GetMapping(value = "/paging")
    public Page<Contact> getContactsPaging(@RequestParam int page, @RequestParam int size, @RequestAttribute AuthProfile authProfile) {

        System.out.println(page);
        System.out.println(size);
        System.out.println(authProfile);

        //기본적으로 key 정렬(default)
        //정렬 설정없이 간다.
        // SQL: ORDER BY email DESC
        //정렬 매개변수 객체
        Sort sort = Sort.by("email").descending();
        System.out.println(sort);
        //페이지 매개변수 객체
        //SQL: OFFSET page * size LIMIT size
        // OFFSET: 어떤 기준점에 대한 거리
        // OFFSET 10: 1번째~10번까지 이후를 말함.
        //LIMT 10: 10건의 레코드
        // LIMIT 10 OFFSET 10 : 앞에서 10번을 건너뛰고, 다음 10건을 조회한다는 뜻
        // 영어 어순이라 의미와 반대 순서임.
        PageRequest pageRequest = PageRequest.of(page, size, sort);
//        PageRequest pageRequest = PageRequest.of(page, size); // sort가 없어도 됨.

        //해당 사용자가 소유자인 연락처 목록만 조회
        return repo.findByOwnerId(authProfile.getId(), pageRequest);

    }

    //GET /contacts/paging/searchByName?page=0&size=10&name=hong
    @GetMapping(value = "/paging/searchByName")
    public Page<Contact> getContactsPagingSearchName
            (@RequestParam int page, @RequestParam int size, @RequestParam String name) {

        System.out.println(page);
        System.out.println(size);
        System.out.println(name);

        //정렬 매개변수 객체
        Sort sort = Sort.by("email").descending();
        //페이지 매개변수 객체
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return repo.findByNameContaining(name, pageRequest);

    }

    @GetMapping(value = "/paging/search")
    public Page<Contact> getContactsPagingSearch
            (@RequestParam int page, @RequestParam int size, @RequestParam String query) {

        System.out.println(page);
        System.out.println(size);
        System.out.println(query);

        //정렬 매개변수 객체
        Sort sort = Sort.by("email").descending();
        //페이지 매개변수 객체
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return repo.findByNameContainsOrPhoneContains(query, query, pageRequest);

    }
    // HTTP 1.1 POST /contacts
    @Auth
    @PostMapping
    public ResponseEntity<Map<String, Object>> addContact(@RequestBody Contact contact, @RequestAttribute AuthProfile authProfile) {
        //@RequestAttribute("authProfile") AuthProfile authProfile attribute속성명(인터셉터의 객체명)과 변수명이 같으면 속성명을 빼고 실행 가능.

        //클라이언트에서 넘어온 JSON이 객체로 잘 변환됐는지 확인
//        System.out.println(contact.getName());
//        System.out.println(contact.getPhone());
//        System.out.println(contact.getEmail());
        System.out.println(authProfile);

        // 1. ------------ 데이터 검증 단계
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



        //해당 사용자의 이메일이 있는지를 확인
//         JPA Query creation을 사용
        if(contact.getEmail()!= null && repo.findById(new ContactId(authProfile.getId(), contact.getEmail())).isPresent()) {

//         Native query를 사용
//        if(contact.getEmail()!= null && repo.findContactByEmail(contact.getEmail()).isPresent()) {

//         JPA Repository의 기본 인터페이스 메서드를 사용
//        if(contact.getEmail() != null && repo.findById(contact.getEmail()).isPresent()) {

            // 맵에 해당 이메일이 있음
            // 이미 있는 데이터를 클라이언트(브라우저) 보냈거나
            // 클라이언트에서 중복 데이터를 보냈거나..
            Map<String, Object> res = new HashMap<>();
            res.put("data", null);
            res.put("message", "동일한 정보가 이미 존재합니다.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        //에러메세지를 자세하게 하면 취약해질 수 있어서, 400번으로 통일하기도 함.


//        repo.save(contact);
        // contact 테이블에 추가
        // 해당 테이블에 현재 key가 동일한 것이 있으면 update(수정)
        // key가 동일한 것이 없으면 insert(추가)


        // 2. --------- 데이터 생성
        //테이블에 레코드 추가
        // key값이 테이블에 이미 있으면 update
        // 없으면 insert 구문이 실행됨.


        //생성자의 id를 설정함
        contact.setOwnerId(authProfile.getId());

        // 테이블에 저장하고 생성된 객체를 반환
        Contact savedContact = repo.save(contact);


        // 3. --------- 응답 처리
        //응답 객체 생성(ResponseEntity)
        //상태코드, 데이터, 메세지
        // 실제로 생성된 레코드(row)를 응답

        //repo.findById(PK값); 이걸로 실제로 생성된 값을 가져올 수 있음.
        //Optional은 null이 될 수 없음.
        // JPA Repository 기본 메서드 사용
//        Optional<Contact> savedContact =
//                repo.findById(contact.getEmail());

        // Native Query를 이용하여 사용
//        Optional<Contact> savedContact =
//                repo.findContactByEmail(contact.getEmail());

        // 생성된 레코드가 존재하는지 여부..
        if(savedContact != null) {
            Map<String, Object> res = new HashMap<>();
            res.put("data", savedContact);
            res.put("message", "created");

            // HTTP Status Code: 201 Created
            // 리소스가 정상적으로 생성되었음.
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

        return ResponseEntity.ok().build();
    }

    /* DELETE /contacts/{email}
	        :Path(경로)Variable(변수)
	   DELETE /contacts/kdkcom@naver.com
	*/

    @Auth
    @DeleteMapping(value = "/{email}")
    //@PathVariable("email") String email
    //경로 문자열{email}과 변수명 String email이 동일하면 안 써도 된다.
    public ResponseEntity removeContact(@PathVariable String email, @RequestAttribute AuthProfile authProfile) {
        System.out.println(email);

        // 해당 키(key)의 데이터가 없으면
//        if(map.get(email) == null) {
        //PK값으로 레코드로 1건 조회해서 없으면

        Optional<Contact> contact = repo.findById(new ContactId(authProfile.getId(), email));
//         JPA Repository 기본 메서드 사용
        if(!contact.isPresent()){

//         Native Query를 이용하여 사용
//        if(!repo.findContactByEmail(email).isPresent()){

//          Query Creation을 이용하여 사용
//        if(!repo.findByEmail(email).isPresent()){
            //404: NOT FOUND, 해당 경로에 리소스가 없다
            //  DELETE /contacts/kdk@naver.com
            // Response Status Code : 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //해당 연락처의 소유자와 삭제를 요청한 사람의 소유자가 일치하는지 확인.
        if(contact.get().getOwnerId() != authProfile.getId()) {
            // 403: Forbidden, 해당 리소스의 권한이 없다.(금지 됐다.)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //레코드(리소스-데이터베이스의 파일의 일부분) 삭제
        repo.deleteById(new ContactId(authProfile.getId(), email));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //PUT(전체수정), PATCH(일부수정)
    // PUT /hong@gmail.com
    // {"name": "길동", "Phone": "010...."}
    //원래는 데이터베이스의 테이블 그대로를 모두 가져와 화면에 표시하진 않음
    //아래처럼 따로 관리해주는 클래스가 있음.
    /*
    @Data
    Class Contact ModifyRequest {
        private String name;
        private string phone;
     }
     */

    //필수값 검증은 수정할 때는 하지 않는다.
    @Auth
    @PutMapping(value = "/{email}")
    public ResponseEntity modifyContact(@PathVariable String email, @RequestBody ContactModifyRequest contact, @RequestAttribute AuthProfile authProfile){

        System.out.println(email);
        System.out.println(contact);

        //1. 키값으로 조회해옴
        Optional<Contact> findedContact = repo.findById(new ContactId(authProfile.getId(), email));
        //2. 해당 레코드가 있는지 확인
        if(!findedContact.isPresent()){
            //404: NOT FOUND, 해당 경로에 리소스가 없다
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //존재하는 레코드
        //Optional 객체에서 실제 있는 값을 꺼내오는 것이 findedContact.get();
        Contact toModifyContact = findedContact.get();
        //3. 조회해온 레코드에 필드값을 수정
        // 매개변수에 name값이 있으면 수정
        if(contact.getName() != null && !contact.getName().isEmpty()){
            toModifyContact.setName(contact.getName());
        }
        // 매개변수에 phone값이 있으면 수정
        if(contact.getPhone() != null && !contact.getPhone().isEmpty()){
            toModifyContact.setPhone(contact.getPhone());
        }
        //(@Id 값이 존재하므로 update를 시도)
        repo.save(toModifyContact);


        //200 OK처리
        return ResponseEntity.ok().build();
    }

}
