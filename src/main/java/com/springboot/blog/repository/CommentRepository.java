package com.springboot.blog.repository;

import com.springboot.blog.entity.CommentEntity;
import com.springboot.blog.payload.CommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPostEntityId( Long id);
}
