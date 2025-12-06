package com.cvgenie.backend.controller;

import com.cvgenie.backend.jwt.JwtHelper;
import com.cvgenie.backend.jwt.JwtRequest;
import com.cvgenie.backend.jwt.JwtResponse;
import com.cvgenie.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping
public class JwtAuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper helper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        try {
            JwtResponse response = userService.login(request);
            return ResponseEntity.ok(response);  // Return the token in the response
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

}


