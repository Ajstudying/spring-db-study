package com.kaj.myapp.post;

import com.kaj.myapp.post.entity.Post;
import com.kaj.myapp.post.entity.PostComment;
import com.kaj.myapp.post.repository.PostCommentRepository;
import com.kaj.myapp.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    PostRepository repo;

    @Autowired
    PostCommentRepository commentRepo;

    @Transactional
    public void createComment(Post post, PostComment comment) {
        commentRepo.save(comment);
        repo.save(post);
    }


}