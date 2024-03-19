package com.springboot.blog.service.impl;

import com.springboot.blog.entity.CommentEntity;
import com.springboot.blog.entity.PostEntity;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    final private CommentRepository commentRepository;

    final private PostRepository postRepository;

    final private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        CommentEntity commentEntity = mapToEntity(commentDto);

        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("PostEntity", "id", postId)
        );

        // set post to comment entity
        commentEntity.setPostEntity(postEntity);

        // comment entity to DB
        CommentEntity newCommentEntity = commentRepository.save(commentEntity);

        return mapToDTO(newCommentEntity);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        // retrieve comments by postId
        List<CommentEntity> commentEntities = commentRepository.findByPostEntityId(postId);

        // convert list of comment entities to list of comment dto's
        return commentEntities.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // retrieve post entity by id
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("PostEntity", "id", postId)
        );

        // retrieve comment by id
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() ->
            new ResourceNotFoundException("CommentEntity", "id", commentId)
        );

        if(!commentEntity.getPostEntity().getId().equals(postEntity.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");

        }

        return mapToDTO(commentEntity);


    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        // retrieve post entity by id
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("PostEntity", "id", postId)
        );

        // retrieve comment by id
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("CommentEntity", "id", commentId)
                );

        if(!commentEntity.getPostEntity().getId().equals(postEntity.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        commentEntity.setName(commentRequest.getName());
        commentEntity.setEmail(commentRequest.getEmail());
        commentEntity.setBody(commentRequest.getBody());

        CommentEntity updateComment = commentRepository.save(commentEntity);
        return mapToDTO(updateComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        // retrieve post entity by id
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("PostEntity", "id", postId)
        );

        // retrieve comment by id
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("CommentEntity", "id", commentId)
        );

        if(!commentEntity.getPostEntity().getId().equals(postEntity.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        commentRepository.delete(commentEntity);

    }

    private CommentDto mapToDTO(CommentEntity commentEntity){
        CommentDto commentDto = modelMapper.map(commentEntity, CommentDto.class);

//        commentDto.setId(commentEntity.getId());
//        commentDto.setName(commentEntity.getName());
//        commentDto.setEmail(commentEntity.getEmail());
//        commentDto.setBody(commentEntity.getBody());
        return commentDto;
    }

    private CommentEntity mapToEntity(CommentDto commentDto){
        CommentEntity commentEntity = modelMapper.map(commentDto, CommentEntity.class);

//        commentEntity.setId(commentDto.getId());
//        commentEntity.setName(commentDto.getName());
//        commentEntity.setEmail(commentDto.getEmail());
//        commentEntity.setBody(commentDto.getBody());
        return commentEntity;
    }
}
