package com.kaj.myapp.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    @Query(value = "select * from post order by no asc", nativeQuery = true)
//    List<Post> getPostSortByNo();

}
