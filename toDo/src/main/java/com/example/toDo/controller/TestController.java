package com.example.toDo.controller;

import com.example.toDo.model.User;
import com.example.toDo.repeository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
//    @PreAuthorize("hasAuthority(\"" + "ROLE_ADMIN" + "\")")
    public ResponseEntity<List<User>> sayHello(){
        List<User> users = this.userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
