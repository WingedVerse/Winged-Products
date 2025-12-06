package com.cvgenie.backend.service;

import com.cvgenie.backend.entity.ApiResponse;
import com.cvgenie.backend.entity.User;
import com.cvgenie.backend.jwt.JwtRequest;
import com.cvgenie.backend.jwt.JwtResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    JwtResponse login(JwtRequest request) throws NullPointerException ;
    ApiResponse<User> register(User user, MultipartFile imageFile);
    ApiResponse<List<User>> getAllUsers();
    ApiResponse<User> getById(long id);
    ApiResponse<User> getByEmail(String email);
    ApiResponse<User> updateUser(long id, User user);
    ApiResponse<User> updateProfilePic(long id,MultipartFile imageFile);
    ApiResponse<String> deleteProfilePic(long userId);

    ApiResponse<Void> deactivateUser(long userId);
    ApiResponse<Void> reactivateUser(long userId);
    ApiResponse<String> userDeactivationStatus(long userId);
    ApiResponse<Void> deleteUser(long userId);
}
