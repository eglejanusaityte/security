package com.security.security.web.controller;

import com.security.security.authentication.AuthenticationService;
import com.security.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  @Autowired
  AuthenticationService service;

  @CrossOrigin(origins = "http://localhost:3030/")
  @PostMapping("/register")
  public ResponseEntity<?> register( @RequestBody User user) {
    Map<String, Serializable> response = service.register(user);
      return ResponseEntity.ok(response);
  }

  @CrossOrigin(origins = "http://localhost:3030/")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    Map<String, Serializable> response = service.login(user);
    return ResponseEntity.ok(response);
  }
}