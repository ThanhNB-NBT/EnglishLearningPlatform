package com.thanhnb.englishlearningplatform.controller;

import com.thanhnb.englishlearningplatform.entity.User;
import com.thanhnb.englishlearningplatform.respository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello! Database connection is successful.";
    }

}

