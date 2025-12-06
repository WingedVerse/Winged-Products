package com.cvgenie.backend.serviceImpl;
import com.cvgenie.backend.entity.Address;
import com.cvgenie.backend.entity.ApiResponse;
import com.cvgenie.backend.entity.User;
import com.cvgenie.backend.jwt.JwtHelper;
import com.cvgenie.backend.jwt.JwtRequest;
import com.cvgenie.backend.jwt.JwtResponse;
import com.cvgenie.backend.repository.UserRepository;
import com.cvgenie.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtHelper jwtHelper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public ApiResponse<User> register(User user, MultipartFile imageFile) {
        if (user == null) {
            logger.warn("Attempted to register a null user.");
            return new ApiResponse<>("error", "Invalid User data", null);
        }
        if (user.getFullName().isEmpty()) {
            logger.warn("Attempted to register a null user Full Name.");
            return new ApiResponse<>("error", "Invalid User Full Name", null);
        }
        if (user.getEmail().isEmpty()) {
            logger.warn("Attempted to register a null User Email.");
            return new ApiResponse<>("error", "Invalid User Email", null);
        }
        if (user.getPhone() == 0) {
            logger.warn("Attempted to register a null User Phone.");
            return new ApiResponse<>("error", "Invalid User Phone", null);
        }
//        if (user.getAddress() == null) {
//            logger.warn("Attempted to register a null User Address.");
//            return new ApiResponse<>("error", "Invalid User Address", null);
//        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.warn("Attempted to register a null User Password.");
            return new ApiResponse<>("error", "Invalid User Password", null);
        }

        // Normalize input
        user.setEmail(user.getEmail().trim().toLowerCase());
        // Duplicate checks
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Duplicate Email registration attempt: {}", user.getEmail());
            return new ApiResponse<>("error", "Email already registered", null);
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            logger.warn("Duplicate Phone registration attempt: {}", user.getPhone());
            return new ApiResponse<>("error", "Phone number already registered", null);
        }
        // Confirm password check
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            logger.warn("Password set attempt: {}", user.getPassword());
            return new ApiResponse<>("error", "Confirm password failed", null);
        }

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File folder = new File(uploadDir);
                if (!folder.exists()) folder.mkdirs();
                // generating unique name
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, imageFile.getBytes());
                // Save file name in DB
                user.setProfilePic(fileName);
            }
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            // Do NOT save confirmPassword
            user.setConfirmPassword(null);
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());
            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", savedUser.getEmail());
            return new ApiResponse<>("success", "User registered successfully", savedUser);
        } catch (Exception e) {
            logger.error("Failed to save User: {}", e.getMessage(), e);
            throw new RuntimeException("Error registering User. Please try again later.", e);
        }
    }


    @Override
    public JwtResponse login(JwtRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        // Check if the user exists and the password matches
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Generate JWT token
            JwtResponse response = new JwtResponse();
            response.setJwtToken(jwtHelper.generateToken(request.getEmail()));
            response.setUserId(user.getId());
            return response;
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }

    @Override
    public ApiResponse<User> getByEmail(String email) {
        logger.warn("Get User by Email attempt: {}",email);
        if (email == null || email.equals("")) {
            return new ApiResponse<>("error", "Invalid Email", null);
        }
        try{
            User user = userRepository.findByEmail(email);
            if (user == null){
                return new ApiResponse<>("error", "Invalid Email", null);
            }
            else {
                return new ApiResponse<>("success", "User fetch successful", user);
            }
        }catch (Exception e){
            logger.error("Failed to Fetch user: {}", e.getMessage(), e);
            throw new RuntimeException("Error Fetching User. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<User> getById(long id) {
        logger.warn("Get User data by Id attempt: {}",id);
        if (id == 0) {
            return new ApiResponse<>("error", "Invalid Id", null);
        }
        try{
            User user = userRepository.findById(id);
            if (user == null){
                return new ApiResponse<>("error", "Invalid Id", null);
            }
            else {
                return new ApiResponse<>("success", "User fetch successful", user);
            }
        }catch (Exception e){
            logger.error("Failed to Fetch user: {}", e.getMessage(), e);
            throw new RuntimeException("Error Fetching User. Please try again later.", e);
        }
    }

    @Override
    @Transactional
    public ApiResponse<User> updateUser(long id, User user){
        if (id <= 0) {
            logger.warn("Invalid User ID: {}", id);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        if (user == null) {
            logger.warn("Attempted to update a null user.");
            return new ApiResponse<>("error", "Invalid User data", null);
        }
        User existingUser = getById(id).getData();

        Address existingAdd = (existingUser.getAddress() != null) ? existingUser.getAddress() : new Address();

        if (user.getFullName() != null) {
            logger.warn("Attempted to update a user full name.");
            existingUser.setFullName(user.getFullName());
        }
        // Duplicate checks
        if (user.getEmail() != null && !user.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
            user.setEmail(user.getEmail().trim().toLowerCase());
            if (userRepository.existsByEmail(user.getEmail())) {
                logger.warn("Duplicate Email registration attempt: {}", user.getEmail());
                return new ApiResponse<>("error", "Email already registered", null);
            } else {
                existingUser.setEmail(user.getEmail());
            }
        }
        if ( user.getPhone() !=0 && user.getPhone() != existingUser.getPhone()){
            if (userRepository.existsByPhone(user.getPhone())) {
                logger.warn("Duplicate Phone registration attempt: {}", user.getPhone());
                return new ApiResponse<>("error", "Phone number already registered", null);
            }
            else {
                existingUser.setPhone(user.getPhone());
            }
        }
        if (user.getAddress() != null) {
            if (user.getAddress().getCountry() != null && !user.getAddress().getCountry().isEmpty()){
                existingAdd.setCountry(user.getAddress().getCountry());
            }
            if (user.getAddress().getState() != null && !user.getAddress().getState().isEmpty()){
                existingAdd.setState(user.getAddress().getState());
            }
            if (user.getAddress().getCity() != null && !user.getAddress().getCity().isEmpty()){
                existingAdd.setCity(user.getAddress().getCity());
            }
            if (user.getAddress().getStreet() != null && !user.getAddress().getStreet().isEmpty()){
                existingAdd.setStreet(user.getAddress().getStreet());
            }
            if (user.getAddress().getDoorNo() != null && !user.getAddress().getDoorNo().isEmpty()){
                existingAdd.setDoorNo(user.getAddress().getDoorNo());
            }
            if (user.getAddress().getState() != null && user.getAddress().getZipCode() != 0){
                existingAdd.setZipCode(user.getAddress().getZipCode());
            }
            existingUser.setAddress(existingAdd);
        }
            try {
                user.setActive(true);
                User savedUser = userRepository.save(existingUser);
                logger.info("User Updated successfully: {}", savedUser.getEmail());
                return new ApiResponse<>("success", "User Updated successfully", savedUser);
            } catch (Exception e) {
                logger.error("Failed to Update User: {}", e.getMessage(), e);
                throw new RuntimeException("Error Updating User. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<User> updateProfilePic(long id, MultipartFile imageFile) {
        logger.warn("Attempted to update a User Profile pic.");
        if (id <= 0) {
            logger.warn("Invalid User ID: {}", id);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        if (imageFile == null) {
            logger.warn("Attempted to update a Invalid Picture File.");
            return new ApiResponse<>("error", "Invalid Picture File.", null);
        }
        User existingUser = getById(id).getData();

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                deleteProfilePic(id);
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File folder = new File(uploadDir);
                if (!folder.exists()) folder.mkdirs();
                // generating unique name
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, imageFile.getBytes());
                // Save file name in DB
                existingUser.setProfilePic(fileName);
            }
            User savedUser = userRepository.save(existingUser);
            logger.info("User Updated successfully: {}", savedUser);
            return new ApiResponse<>("success", "User Profile pic Updated successfully", savedUser);
        } catch (Exception e) {
            logger.error("Failed to Update Profile pic: {}", e.getMessage(), e);
            throw new RuntimeException("Error Updating Profile pic. Please try again later.", e);
        }    }

    @Override
    public ApiResponse<List<User>> getAllUsers() {
        logger.warn("Get All Users attempt");
        try{
            List<User> userList = userRepository.findAll();
            if (userList == null){
                return new ApiResponse<>("Success", "No Users Yet", null);
            }
            else {
                return new ApiResponse<>("success", "Users fetch successful", userList);
            }
        }catch (Exception e){
            logger.error("Failed to Fetch users: {}", e.getMessage(), e);
            throw new RuntimeException("Error Fetching Users. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<String> deleteProfilePic(long userId) {
        if (userId <= 0) {
            logger.warn("Invalid User ID: {}", userId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        try {
            User user = getById(userId).getData();
            if (user == null) {
                return new ApiResponse<>("error", "User Not Found", null);
            }
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            // Delete file if exists
            if (user.getProfilePic() != null) {
                File file = new File(uploadDir + user.getProfilePic());
                if (file.exists()) file.delete();
            }
            // Remove file name from DB
            user.setProfilePic(null);
            userRepository.save(user);

            logger.info("User image removed: {}", userId);
            return new ApiResponse<>("success", "Image Deleted Successfully", null);

        } catch (Exception e) {
            logger.error("Failed to delete User image: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting Image. Try later.", e);
        }
    }

    @Override
    public ApiResponse<Void> deactivateUser(long userId) {
        logger.error("deactivate user attempt, ID: {}", userId);
        User user = getById(userId).getData();
        if (user == null) {
            return new ApiResponse<>("error", "User Not Found", null);
        }
        try {
            user.setDeactivated(true);
            userRepository.save(user);
            logger.info("User Account deactivated successfully, ID: {}", userId);
            return new ApiResponse<>("success", "User Account deactivated successfully", null);
        }catch (Exception e){
            logger.error("Failed to Deactivate User: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deactivating User. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Void> reactivateUser(long userId) {
        logger.error("reactivate user attempt, ID: {}", userId);
        User user = getById(userId).getData();
        if (user == null) {
            return new ApiResponse<>("error", "User Not Found", null);
        }
        try {
            user.setDeactivated(false);
            userRepository.save(user);
            logger.info("User Account reactivated successfully, ID: {}", userId);
            return new ApiResponse<>("success", "User Account reactivated successfully", null);
        }catch (Exception e){
            logger.error("Failed to Reactivate User: {}", e.getMessage(), e);
            throw new RuntimeException("Error Reactivating User. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<String> userDeactivationStatus(long userId) {
        logger.error("Get User Deactivation Status attempt, ID: {}", userId);
        User user = getById(userId).getData();
        if (user == null) {
            return new ApiResponse<>("error", "User Not Found", null);
        }
        try {
            String status="";
            if (user.isDeactivated()){
                status = "User Account Deactivated";
            }else {
                status = "User Account is Active";
            }
            logger.info("User Deactivation status fetched successfully, ID: {}", userId);
            return new ApiResponse<>("success", "User Deactivation status fetched successfully", status);
        }catch (Exception e){
            logger.error("Failed to Fetch User Deactivation Status: {}", e.getMessage(), e);
            throw new RuntimeException("Error Fetching User Deactivation Status. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Void> deleteUser(long userId) {
        // Validate ID
        if (userId <= 0) {
            logger.warn("Invalid User ID to delete: {}", userId);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        // Check if client exists
        User user = userRepository.findById(userId);
        if (user == null) {
            logger.warn("Attempt to delete non-existent user ID: {}", userId);
            return new ApiResponse<>("error", "User not found", null);
        }
        // Delete client
        try {
            userRepository.deleteById(userId);
            logger.info("User Account deleted successfully, ID: {}", userId);
            return new ApiResponse<>("success", "User Account deleted successfully", null);
        }catch (Exception e){
            logger.error("Failed to Delete User: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting User. Please try again later.", e);
        }    }


}