package com.kaj.myapp.post;



import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthProfile;
import com.kaj.myapp.auth.entity.LoginRepository;
import com.kaj.myapp.post.entity.Post;
import com.kaj.myapp.post.entity.PostComment;
import com.kaj.myapp.post.repository.PostCommentRepository;
import com.kaj.myapp.post.repository.PostRepository;
import com.kaj.myapp.post.request.PostModifyRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
@RequestMapping(value = "/posts")
public class PostController {

    @Autowired
    PostRepository repo;
    @Autowired
    LoginRepository logRepo;
    @Autowired
    PostCommentRepository commentRepo;
    @Autowired
    PostService service;



    @GetMapping
    public List<Post> getPostList() {

        //JPA 사용
//        List<Post> list = repo.findAll(Sort.by("no").ascending());
        //Query 사용
        List<Post> list = repo.findPostSortByNo();

        return list;
    }

    @GetMapping(value = "/paging")
    public Page<Post> getPostsPaging(@RequestParam int page, @RequestParam int size){
        System.out.println(page + "1");
        System.out.println(size + "1");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findAll(pageRequest);
    }
//    @GetMapping(value = "/paging/search")
//    public Page<Post> getPostsPagingCreator
//            (@RequestParam int page, @RequestParam int size, @RequestParam String query){
//        System.out.println(page + "2");
//        System.out.println(size + "2");
//        System.out.println(query + "2");
//
//        Sort sort = Sort.by("no").descending();
//        PageRequest pageRequest = PageRequest.of(page, size, sort);
//        return  repo.findByCreatorNameContains(query, pageRequest);
//    }
    @GetMapping(value = "/paging/search")
    public Page<Post> getPostsPagingSearch
            (@RequestParam int page, @RequestParam int size, @RequestParam String query){
        System.out.println(page + "3");
        System.out.println(size + "3");
        System.out.println(query + "3");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findByCreatorNameContainsOrContentContains(query, query, pageRequest);
    }

    //title, content 필수 속성
    @Auth
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post, @RequestAttribute AuthProfile authProfile) {

        //1. 입력값 검증(title, content)
        // -> 입력값 오류(빈 값)가 있으면 400 에러 띄움
        //제목과 컨텐트 내용이 비어있으면 내보내는 400번 코드
        System.out.println(authProfile);

        if(post.getTitle() == null || post.getContent() == null || post.getTitle().isEmpty() || post.getContent().isEmpty()){
//            Map<String, Object> result = ;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //2. 채번: 번호를 딴다(1...2, 3...)
        //데이터베이스의 auto_increment를 사용할 것이므로
        // 아래 2줄은 필요없게 된다. AtomicLong 필요없음.
//        long no = num.incrementAndGet();
//        post.setNo(no);

        //3. 시간값, 게시자 이름 설정(set필드명(..))

        post.setCreatorName(authProfile.getNickname());
        post.setCreatedTime(new Date().getTime());

        post.setCreatorId(authProfile.getId());
        //생성된 객체를 반환
        Post savedPost = repo.save(post);



        //생성된 객체가 존재하면 null값이 아닐 때
        if(savedPost != null){
            Map<String, Object> res = new HashMap<>();
            res.put("data", savedPost);
            res.put("message", "created");

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }


        return ResponseEntity.ok().build(); // 이렇게하면 그냥 생성되고 끝!
    }

    @Auth
    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable long no, @RequestAttribute AuthProfile authProfile) {
        System.out.println(no);

        Optional<Post> post = repo.findPostByNo(no);


        if(!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

//        if(post.get().getNo() != no){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Auth
    @PutMapping(value = "/{no}")
    public  ResponseEntity modifyPost(@PathVariable long no, @RequestBody PostModifyRequest post, @RequestAttribute AuthProfile authProfile){
        System.out.println(no);
        System.out.println(post);

        // 1. 키값으로 조회해옴
        Optional<Post> findedPost =  repo.findById(authProfile.getId());
        // 2. 해당 레코드가 있는지 확인
        if(!findedPost.isPresent()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //수정해서 저장할 포스트
        Post toModifyPost = findedPost.get();

        if(post.getTitle() != null && !post.getTitle().isEmpty()){
            toModifyPost.setTitle(post.getTitle());
        }
        if(post.getContent() != null && !post.getContent().isEmpty()){
            toModifyPost.setContent(post.getContent());
        }
        //update
        repo.save(toModifyPost);

        //ok 처리
        return ResponseEntity.ok().build();
    }

    // 하위객체(테이블) 추가하기
    // POST /posts/{no}/comments
    @Auth
    @PostMapping("/{no}/comments")
    public ResponseEntity addComments(
            @PathVariable long no,
            @RequestBody PostComment postComment,
            @RequestAttribute AuthProfile authProfile) {

        Optional<Post> post = repo.findById(no);
        if(!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 커멘트 추가
        postComment.setPost(post.get());
        postComment.setOwnerId(authProfile.getId());
        postComment.setOwnerName(authProfile.getNickname());

        // 커멘트 건수 증가 및 최근 커멘트 표시
        Post findedPost = post.get();
        findedPost.setLatestComment(postComment.getContent());
        findedPost.setCommentCnt(post.get().getCommentCnt() + 1);

        // 트랜잭션 처리
        service.createComment(findedPost, postComment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
