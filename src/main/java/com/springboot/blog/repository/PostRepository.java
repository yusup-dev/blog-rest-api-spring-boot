package com.springboot.blog.repository;

import com.springboot.blog.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("SELECT p FROM PostEntity p WHERE " +
            "p.title LIKE CONCAT('%',:query, '%')" +
            "Or p.description LIKE CONCAT('%', :query, '%')")
    List<PostEntity> searchPosts(String query);

    @Query(value = "SELECT * FROM posts p WHERE " +
            "p.title LIKE CONCAT('%',:query, '%')" +
            "Or p.description LIKE CONCAT('%', :query, '%')", nativeQuery = true)
    List<PostEntity> searchPostsSQL(String query);

    List<PostEntity> findByCategoryId(Long categoryId);
}
