package com.cvgenie.backend.controller;
import com.cvgenie.backend.entity.ApiResponse;
import com.cvgenie.backend.entity.Portfolio;
import com.cvgenie.backend.entity.User;
import com.cvgenie.backend.service.UserService;
import com.cvgenie.backend.serviceImpl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<User>> register(@RequestPart("user") String userJson, @RequestPart(value = "imageFile", required = false) MultipartFile imageFile){
        try {
            ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(userJson, User.class);
        logger.info("Received registration request for: {}", user);
        ApiResponse<User> response = userService.register(user,imageFile);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> userById(@PathVariable long id) {
        logger.info("Received get user by Id request: {}",id);
        ApiResponse<User> response = userService.getById(id);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user-by-email/{email}")
    public ResponseEntity<ApiResponse<User>> userByEmail(@PathVariable String email) {
        logger.info("Received get user by email request: {}",email);
        ApiResponse<User> response = userService.getByEmail(email);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<List<User>>> allUsers() {
        logger.info("Received get all users request :");
        ApiResponse<List<User>> response = userService.getAllUsers();
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update-user/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable long id, @RequestBody User user) {
        try {
            logger.info("Received update User Data request for: {}", id);
            ApiResponse<User> response = userService.updateUser(id, user);
            if ("error".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/update-user-profile-pic/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<User>> updateUserProfilePic(@PathVariable long id, @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            logger.info("Received update Profile Pic request for: {}", id);
            ApiResponse<User> response = userService.updateProfilePic(id, imageFile);
            if ("error".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/deactivate-user/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable long id) {
        ApiResponse<Void> response = userService.deactivateUser(id);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/reactivate-user/{id}")
    public ResponseEntity<ApiResponse<Void>> reactivateUser(@PathVariable long id) {
        ApiResponse<Void> response = userService.reactivateUser(id);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user-deactivation-status/{id}")
    public ResponseEntity<ApiResponse<String>> getUserDeactivationStatus(@PathVariable long id) {
        ApiResponse<String> response = userService.userDeactivationStatus(id);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable long id) {
        ApiResponse<Void> response = userService.deleteUser(id);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
