package com.in28minutes.rest.webservices.restfulwebservices.post;

import com.in28minutes.rest.webservices.restfulwebservices.user.User;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserNotFoundException;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

    private UserRepository userRepository;

    private PostRepository postRepository;

    public PostController(UserRepository userRepository, PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> getUserPosts(@PathVariable Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty())
            throw new UserNotFoundException("User with id " + id + " not found");
        return optionalUser.get().getUserPosts();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createUserPost(@PathVariable Integer id, @RequestBody @Valid Post post){
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty())
            throw new UserNotFoundException("User with id " + id + " not found");

        post.setUser(optionalUser.get());

        Post savedPost = postRepository.save(post);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId()).toUri()).build();
    }

    @GetMapping("/users/{userId}/posts/{postId}")
    public Post getUserPost(@PathVariable(value="userId") Integer userId, @PathVariable(value="postId") Integer postId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty())
            throw new UserNotFoundException("User with id " + userId + " not found");
        return optionalUser.get().getUserPosts().stream().filter(post -> post.getId().equals(postId)).findFirst().orElse(null);
    }
}
