package com.in28minutes.rest.webservices.restfulwebservices.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserResource {

    /*
    @Autowired
    private UserDaoService userDaoService;
     */

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUserById(@PathVariable Integer id){
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty())
            throw new UserNotFoundException("User with id " + id + " not found.");

        EntityModel<User> entityModel = EntityModel.of(user.get());

        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(methodOn(this.getClass()).retrieveAllUsers());

        entityModel.add(linkBuilder.withRel("all-users"));

        return entityModel;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws URISyntaxException {
        User savedUser = userRepository.save(user);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri()).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        userRepository.deleteById(id);
    }
}
