package com.kaj.myapp.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    //(key)번호
    @Id
    // database의 auto-increment를 사용함.
    // auto_increment: 레코드가 추가될 때 자동으로 증가되는 값을 사용.
    // 1, 2, 3....
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long no;
    //제목(not nullable)
    @Column(nullable = false)
    private String title;
    //내용
    @Column(nullable = false)
    private String content;
    //게시자 (not nullable)
//    @Column(nullable = false)
    // 게시글을 작성 사용자의 nickname
    private String creatorName;
    // 게시글을 작성한 사용자의 Id
    private long creatorId;

    //생성시간(unix epoch time)

    //created_time bigint not null,
    //primitive type: int, char, long, double
    //nullable 불가, long 기본값이 0 그래서 데이터 베이스에서도 not null로 세팅해줌.
    private long createdTime;
    //데이터 베이스에 null을 넣고 싶으면 Class 타입을 써야함. ex) 위의 값은 Long타입으로 쓴다.
    @Column(length = 1024 * 1024 * 20)
    private String image;

    // 댓글 수
    private long commentCnt;
    // 최근 댓글 내용
    private String latestComment;


    //validate(입증하다.)
//    public Map<String, Object> validate() {
//        Map<String, Object> res = new HashMap<>();
//        res.put("status", null);
//        res.put("status", HttpStatus.BAD_REQUEST);
//        return res;
//    }

}


