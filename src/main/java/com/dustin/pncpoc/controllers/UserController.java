package com.dustin.pncpoc.controllers;

import com.dustin.pncpoc.exceptions.UserNotFoundException;
import com.dustin.pncpoc.models.User;
import com.dustin.pncpoc.services.UserServiceImpl;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "User Rest Controller")
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/v1/users")
@RestController
public class UserController {

    private final UserServiceImpl userService;
    //private ModelMapper modelMapper = new ModelMapper();

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {
        User createdUser = this.userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(APPLICATION_JSON)
                .body(createdUser);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.userService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(users);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = this.userService.findByUserId(id)
                .orElseThrow(() -> new UserNotFoundException(id));
//        if (user.isPresent()) { -> When the Optional<User> was being used
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(APPLICATION_JSON)
                    .body(user);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<User> updateUserById(@PathVariable String id, @RequestBody User user) {
        User updatedUser = this.userService.updateUser(user, id);
        if (updatedUser == null) {
            throw new UserNotFoundException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(updatedUser);

    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity deleteUserById(@PathVariable String id) {
        this.userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
