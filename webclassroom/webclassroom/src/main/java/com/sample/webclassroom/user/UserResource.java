package com.sample.webclassroom.user;

// import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


import java.net.URI;
import java.util.List;

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

import com.sample.webclassroom.user.exception.FieldEmptyException;
import com.sample.webclassroom.user.exception.UserNotFoundException;

@RestController
public class UserResource {
	
	@Autowired
	private UserDaoService userService;
	
	//GET users
	//retrieve all users
	@GetMapping(path="/users")
	public List<User> getAllUsers(){
		return userService.findAll();
	}
	
	//get specific user
	@GetMapping(path="/users/{id}")
	public Resource<User> getUser(@PathVariable int id) {
		User user = userService.findOne(id);
//		if(user == null)
//			throw new UserNotFoundException("id-"+id);
//		
//		EntityModel<User> resource =  new EntityModel<User>(user);
//		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
//		resource.add(link.withRel("all-users"));
		Resource<User> resource = new Resource(user);
		ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
		resource.add(link.withRel("get-all"));
		return resource;
	}
	
	@PostMapping(path="/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		
		if(user.getBirthDate() == null)
		{
			throw new FieldEmptyException("Birth Date field cannot be empty");
		}
		
		User savedUser = userService.save(user);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path="/users/{id}")
	public void deleteUser(@PathVariable int id) {
		User user = userService.deleteById(id);
		
		if(user == null)
		{
			throw new UserNotFoundException("id-"+id);
		}
	}
}
