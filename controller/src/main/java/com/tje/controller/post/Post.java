package com.tje.controller.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    //(key)번호
    private long no;
    //제목(not nullable)
    private String title;
    //내용
    private String content;
    //게시자 (not nullable)
    private String creatorName;
    //생성시간(unix epoch time)
    private long createdTime;

    public Map<String, Object> validate() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", null);
        res.put("status", HttpStatus.BAD_REQUEST);
        return res;
    }

}


