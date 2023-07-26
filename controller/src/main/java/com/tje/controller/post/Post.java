package com.tje.controller.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    //(key)번호
    private int no;
    //제목(not nullable)
    private String title;
    //내용
    private String content;
    //게시자 (not nullable)
    private String creatorName;
    //생성시간(unix epoch time)
    private long createdTime;

}
