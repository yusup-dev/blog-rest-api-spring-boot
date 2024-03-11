package com.springboot.blog.service.impl;
import com.springboot.blog.entity.PostEntity;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    final private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
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
    public List<PostDto> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        return postEntities.stream().map(this::mapToDTO).collect(Collectors.toList());
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
        PostDto postDto = new PostDto();
        postDto.setId(postEntity.getId());
        postDto.setTitle(postEntity.getTitle());
        postDto.setDescription(postEntity.getDescription());
        postDto.setContent(postEntity.getContent());
        return postDto;
    }


    // convert DTO to entity
    private PostEntity mapToEntity(PostDto postDto){
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postDto.getTitle());
        postEntity.setDescription(postDto.getDescription());
        postEntity.setContent(postDto.getContent());
        return postEntity;
    }
}
