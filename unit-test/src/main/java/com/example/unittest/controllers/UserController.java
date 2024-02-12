package com.example.unittest.controllers;

import com.example.unittest.entities.User;
import com.example.unittest.repositories.UserRepository;
import com.example.unittest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @PostMapping(path = "/create")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }


    @GetMapping(path = "/getAll")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Optional<User> getSingle(@PathVariable Long id) {
        return userRepository.existsById(id) ? userRepository.findById(id) : Optional.empty();
    }

    @PutMapping(path = "/{id}/updateName")
    public User update(@PathVariable Long id, @RequestParam String firstName) {
        return userService.updateFirstName(id, firstName);
    }

    @PatchMapping(path = "/{id}/updateEmail")
    public User updateEmail(@PathVariable Long id, @RequestParam String email) {
        return userService.setNewEmail(id, email);
    }

    @DeleteMapping(path = "{id}/delete")
    public void delete(@PathVariable Long id){
        userRepository.deleteById(id);
    }


}


