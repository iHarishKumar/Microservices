package com.sample.webclassroom.user;

// import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sample.webclassroom.post.Post;
import com.sample.webclassroom.post.PostRepository;
import com.sample.webclassroom.user.exception.FieldEmptyException;
import com.sample.webclassroom.user.exception.UserNotFoundException;

@RestController
public class UserJPAResource {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	//GET users
	//retrieve all users
	@GetMapping(path="/jpa/users")
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
	
	//get specific user
	@GetMapping(path="/jpa/users/{id}")
	public Resource<User> getUser(@PathVariable int id) {
		Optional<User> user = userRepo.findById(id);
		
		if(!user.isPresent()) {
			throw new UserNotFoundException("id-"+id);
		}
		Resource<User> resource = new Resource(user);
		ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
		resource.add(link.withRel("get-all"));
		return resource;
	}
	
	@PostMapping(path="/jpa/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		
		if(user.getBirthDate() == null)
		{
			throw new FieldEmptyException("Birth Date field cannot be empty");
		}
		
		User savedUser = userRepo.save(user);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path="/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepo.deleteById(id);
	}
	
	// Get all posts of a specific user
	@GetMapping(path="/jpa/users/{id}/posts")
	public List<Post> retrieveAllPosts(@PathVariable int id){
		Optional<User> user = userRepo.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("id-"+id);
		}
		
		return user.get().getPosts();
	}
	
	// Create post for a specific user
	@PostMapping(path="/jpa/users/{id}/posts")
	public ResponseEntity<?> createPost(@PathVariable int id, @RequestBody Post post) {
		Optional<User> userOptional = userRepo.findById(id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id-"+id);
		}
		
		User user = userOptional.get();
		
		post.setUser(user);
		
		postRepo.save(post);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("{id}")
				.buildAndExpand(post.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
}
