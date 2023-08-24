package com.kaj.myapp.post.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostModifyRequest {
    private String title;
    private String content;
}
