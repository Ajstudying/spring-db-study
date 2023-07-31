package com.kaj.myapp.post;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping(value = "/posts")
public class PostController {

    @Autowired
    PostRepository repo;



    @GetMapping
    public List<Post> getPostList() {

        List<Post> list = repo.findAll(Sort.by("no").ascending());

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
        //데이터베이스의 auto_increment를 사용할 것이므로
        // 아래 2줄은 필요없게 된다. AtomicLong 필요없음.
//        long no = num.incrementAndGet();
//        post.setNo(no);

        //3. 시간값, 게시자 이름 설정(set필드명(..))

        post.setCreatorName("Dodo");
        post.setCreatedTime(new Date().getTime());

        repo.save(post);
        System.out.println(repo);


        Optional<Post> savedPost = repo.findById((post.getNo()));
        //레코드가 존재하는지 여부
        if(savedPost.isPresent()){
            Map<String, Object> res = new HashMap<>();
            res.put("data", repo.findById((post.getNo())));
            res.put("message", "created");

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }


        return ResponseEntity.ok().build(); // 이렇게하면 그냥 생성되고 끝!
    }

    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable long no) {
        System.out.println(no);
        if(!(repo.findById(no).isPresent())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
