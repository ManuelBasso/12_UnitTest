package com.example.unittest.services;

import com.example.unittest.entities.User;
import com.example.unittest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User setNewEmail(Long id, String email) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            user.get().setEmail(email);
            return userRepository.save(user.get());
        } else {
            return null;
        }
    }

    public User updateFirstName(Long id, String firstName) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            user.get().setFirstName(firstName);
            return userRepository.save(user.get());
        } else {
            return null;
        }
    }
}
