package com.springboot.blog.service.impl;
import com.springboot.blog.entity.PostEntity;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    final private PostRepository postRepository;

    final private ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public PostDto createPost(PostDto postDto) {

        // convert DTO to entity
        PostEntity postEntity = mapToEntity(postDto);
        PostEntity newPostEntity = postRepository.save(postEntity);

        // convert entity to DTO
        PostDto postResponse = mapToDTO(newPostEntity);
        return postResponse;
    }
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<PostEntity> posts = postRepository.findAll(pageable);

        // get content for page object
        List<PostEntity> listOfPosts = posts.getContent();

        List<PostDto> content= listOfPosts.stream().map(this::mapToDTO).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("PostEntity", "id", id));
        return mapToDTO(postEntity);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PostEntity", "id", id));

        postEntity.setTitle(postDto.getTitle());
        postEntity.setDescription(postDto.getDescription());
        postEntity.setContent(postDto.getContent());

        PostEntity updatePostEntity = postRepository.save(postEntity);
        return mapToDTO(updatePostEntity);
    }

    @Override
    public void deletePostById(Long id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("PostEntity", "id", id));
        postRepository.delete(postEntity);
    }

    // convert Entity int DTO
    private PostDto mapToDTO(PostEntity postEntity){
        PostDto postDto = modelMapper.map(postEntity, PostDto.class);
//        postDto.setId(postEntity.getId());
//        postDto.setTitle(postEntity.getTitle());
//        postDto.setDescription(postEntity.getDescription());
//        postDto.setContent(postEntity.getContent());
        return postDto;
    }


    // convert DTO to entity
    private PostEntity mapToEntity(PostDto postDto){
        PostEntity postEntity = modelMapper.map(postDto, PostEntity.class);


//        postEntity.setTitle(postDto.getTitle());
//        postEntity.setDescription(postDto.getDescription());
//        postEntity.setContent(postDto.getContent());
        return postEntity;
    }
}
