package com.kaj.mypet.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/petposts")
public class PostController {

    @Autowired
    PostRepository repo;

    @GetMapping
    public List<Post> getPostList() {

        List<Post> list = repo.findAll(Sort.by("no").ascending());
        return list;
    }

    @PostMapping
    public ResponseEntity addPost(@RequestBody Post post){

        if(post.getTitle() == null || post.getTitle().isEmpty() || post.getContent() == null || post.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return null;
    }



}
