package com.kaj.myapp.post.entity;

import com.kaj.myapp.post.entity.Post;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    @ManyToOne
    private Post post;

    private long ownerId;
    private String ownerName;
}