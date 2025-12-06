package com.cvgenie.backend.serviceImpl;

import com.cvgenie.backend.entity.ApiResponse;
import com.cvgenie.backend.entity.Favourite;
import com.cvgenie.backend.entity.ItemType;
import com.cvgenie.backend.entity.User;
import com.cvgenie.backend.repository.FavouriteRepository;
import com.cvgenie.backend.repository.UserRepository;
import com.cvgenie.backend.service.FavouriteService;
import com.cvgenie.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavouriteServiceImpl implements FavouriteService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private FavouriteRepository favouriteRepository;
    @Autowired
    private UserService userService;

    @Override
    public ApiResponse<Favourite> addToFavourites(long userId, Favourite favourite) {
        if (userId <= 0) {
            logger.warn("Invalid User ID: {}", userId);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        User existingUser = userService.getById(userId).getData();
        if (existingUser == null) {
            logger.warn("User Not Found");
            return new ApiResponse<>("error", "User Not Found", null);
        }
        if (favourite == null) {
            logger.warn("Attempted to add a null Favourite.");
            return new ApiResponse<>("error", "Invalid Favourite data", null);
        }
        if (favourite.getItemType() == null) {
            logger.warn("Favourite Item Type Required");
            return new ApiResponse<>("error", "Favourite Item Type Required", null);
        }
        if (favourite.getItemId() == null || favourite.getItemId()<0) {
            logger.warn("Invalid Item Id");
            return new ApiResponse<>("error", "Invalid Item Id", null);
        }else {
            Favourite fav = getFavouriteByUserIdAndItemId(userId,favourite.getItemId()).getData();
            if (fav != null){
                return new ApiResponse<>("error", "Item already in Favourite", null);
            }
        }
        try {
            favourite.setUser(existingUser);
            Favourite savedFavourite = favouriteRepository.save(favourite);
            logger.info("Item Added to Favourites successfully: {}", savedFavourite);
            return new ApiResponse<>("success", "Item Added to Favourites successfully", savedFavourite);
        } catch (Exception e) {
            logger.error("Failed to Add to Favourites: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to Add to Favourites.", e);
        }    }

    @Override
    public ApiResponse<Favourite> removeFavourite(long userId, long itemId) {
        if (userId <= 0) {
            logger.warn("Invalid User ID: {}", userId);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        if (itemId <= 0) {
            return new ApiResponse<>("error", "Invalid Item ID", null);
        }
        try {
                Favourite favourite = favouriteRepository.findByUserIdAndItemId(userId,itemId);
                if (favourite == null){
                    return new ApiResponse<>("error", "Favourite Not Found", null);
                }
                favouriteRepository.deleteById(favourite.getId());
                return new ApiResponse<>("success", "Favourite Removed Successfully", favourite);
        } catch (Exception e) {
            logger.error("Failed to remove Favourite: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to remove Favourite.", e);
        }
    }

    @Override
    public ApiResponse<List<Favourite>> getFavourites(long userId) {
        if (userId <= 0) {
            logger.warn("Invalid User ID: {}", userId);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        User existingUser = userService.getById(userId).getData();
        if (existingUser == null) {
            logger.warn("User Not Found");
            return new ApiResponse<>("error", "User Not Found", null);
        }
        try {
            List<Favourite> favourites = favouriteRepository.findByUserId(userId);
            if (favourites.isEmpty()){
                return new ApiResponse<>("success", "No favourites Yet", null);
            }else {
                return new ApiResponse<>("success", "Favourites List Found", favourites);
            }
        } catch (Exception e) {
            logger.error("Failed to get Favourites: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get Favourites.", e);
        }
    }

    @Override
    public ApiResponse<Favourite> getFavouriteById(long favouriteId) {
        try {
            Favourite favourite = favouriteRepository.findById(favouriteId).orElse(null);
            if (favourite == null){
                return new ApiResponse<>("error", "Invalid Favourite` ID", null);
            }else {
                return new ApiResponse<>("success", "Favourite Item Found", favourite);
            }
        } catch (Exception e) {
            logger.error("Failed to get Favourite Item: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get Favourite Item.", e);
        }
    }

    @Override
    public ApiResponse<Favourite> getFavouriteByUserIdAndItemId(long userId, long itemId) {
        if (userId <= 0) {
            logger.warn("Invalid User ID: {}", userId);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        if (itemId <= 0) {
            return new ApiResponse<>("error", "Invalid Item ID", null);
        }
        try {
            Favourite favourite = favouriteRepository.findByUserIdAndItemId(userId,itemId);
            if (favourite == null){
                return new ApiResponse<>("error", "Favourite Not Found", null);
            }
            return new ApiResponse<>("success", "Favourite Found", favourite);
        } catch (Exception e) {
            logger.error("Failed to fetch Favourite: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch Favourite.", e);
        }    }

    @Override
    public ApiResponse<List<Favourite>> getFavouriteByType(long userId, ItemType itemType) {
        if (userId <= 0) {
            logger.warn("Invalid User ID: {}", userId);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        User existingUser = userService.getById(userId).getData();
        if (existingUser == null) {
            logger.warn("User Not Found");
            return new ApiResponse<>("error", "User Not Found", null);
        }
        try {
            List<Favourite> favourites = favouriteRepository.findByUserIdAndItemType(userId,itemType);
            if (favourites.isEmpty()){
                return new ApiResponse<>("success", "No favourites Yet", null);
            }else {
                return new ApiResponse<>("success", "Favourites List Found", favourites);
            }
        } catch (Exception e) {
            logger.error("Failed to get Favourites: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get Favourites.", e);
        }
    }
}
