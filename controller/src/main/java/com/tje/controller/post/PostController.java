package com.tje.controller.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// GET /posts
// 게시글 목록이 JSON으로 나오게
@RestController
@RequestMapping(value = "/posts")
public class PostController {
    //동시성을 위한 자료 구조
    //HashMap -> ConcurrentHashMap
    //Integer -> AtomicInteger
    Map<Long, Post> map = new ConcurrentHashMap<>();
    AtomicLong num = new AtomicLong(0);

    //box로 만들기..
    /*creatorName: 서버에서 아무나 넣고
            게시자
            ----
            제목(h3)
            본문(p)
            ----
            생성시간
    */



    @GetMapping
    public List<Post> getPostList() {
        // 증가시키고 값 가져오기
//        long no = num.incrementAndGet();
//        map.clear();

//        map.put(no, new Post(0001, "JAVA", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Architecto perspiciatis assumenda at ex aliquid nemo cupiditate dignissimos repellendus iste. Sunt magnam voluptatum nisi itaque ipsam est iure aspernatur tempora magni!", "John", new Date().getTime()));

//        map.put(no, Post.builder()
//                .title("Spring-Boot")
//                .no(no)
//                .creatorName("Mary")
//                .title("Lorem, ipsum dolor.")
//                .content("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Architecto perspiciatis assumenda at ex aliquid nemo cupiditate dignissimos repellendus iste. Sunt magnam voluptatum nisi itaque ipsam est iure aspernatur tempora magni!")
//                .createdTime(new Date().getTime())
//                .build());

//        map.put(no, Post.builder()
//                .creatorName("Lily")
//                .no(no)
//                .title("Spring")
//                .createdTime(new Date().getTime())
//                .content("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Architecto perspiciatis assumenda at ex aliquid nemo cupiditate dignissimos repellendus iste. Sunt magnam voluptatum nisi itaque ipsam est iure aspernatur tempora magni!")
//                .build());

        var list = new ArrayList<>(map.values());
        //람다식(lambda expression)
        //식이 1개인 함수식:
        //매개변수 영역과 함수 본체를 화살표로 구분함
        //함수 본체의 수식 값이 반환값
        list.sort((a, b) -> (int)(b.getNo() - a.getNo()));

        return list;
    }

    //title, content 필수 속성
    //creatorName: 서버에서 가짜이름 넣음.
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post) {

        //1. 입력값 검증(title, content)
        // -> 입력값 오류(빈 값)가 있으면 400 에러 띄움
        //제목과 컨텐트 내용이 비어있으면 내보내는 400번 코드

        if(post.getTitle() == null || post.getContent() == null || post.getTitle().isEmpty() || post.getContent().isEmpty()){
//            Map<String, Object> result = ;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(post.validate());
        }
        //2. 채번: 번호를 딴다(1...2, 3...)
        long no = num.incrementAndGet();
        post.setNo(no);

        //3. 시간값, 게시자 이름 설정(set필드명(..))

        post.setCreatorName("Dodo");
        post.setCreatedTime(new Date().getTime());

        //4.맵에 추가(서버에서 생성된 값을 설정

        map.put(no, post);
//        System.out.println(post);
        //5. 생성된 객체를 맵에서 찾아서 반환
        //객체 추가

        //응답 객체 생성
        Map<String, Object> res = new HashMap<>();
        res.put("data", map.get(post.getNo()));
        res.put("message", "created");


        return ResponseEntity.status(HttpStatus.CREATED).body(res); //메세지 보냄!
//        return ResponseEntity.status(HttpStatus.CREATED).build(); // 이렇게하면 그냥 생성되고 끝!
    }

    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable long no) {
        System.out.println(no);
        if(map.get(no) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        map.remove(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
