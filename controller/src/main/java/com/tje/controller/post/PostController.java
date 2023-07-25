package com.tje.controller.post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// GET /posts
// 게시글 목록이 JSON으로 나오게
@RestController
@RequestMapping(value = "/posts")
public class PostController {
    List<Post> list = new ArrayList<>();

    @GetMapping
    public List<Post> getPostList() {
        list.clear();

        list.add(new Post(0001, "JAVA", 1234, "김철수", new Date().getTime()));
        list.add(Post.builder()
                .title("Spring-Boot")
                .no(0002)
                .creatorName("Mary")
                .content(5678)
                .createdTime(new Date().getTime())
                .build());

        return list;
    }
}
