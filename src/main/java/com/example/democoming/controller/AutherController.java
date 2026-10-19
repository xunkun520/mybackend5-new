package com.example.democoming.controller;

import com.example.democoming.entity.User;
import com.example.democoming.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AutherController {
    @Autowired
    private Userservice userservice;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userservice.login(user);
    }
@PostMapping("/register")
    public String register(@RequestBody User user) throws IOException {
        return userservice.register(user);
    }

}
