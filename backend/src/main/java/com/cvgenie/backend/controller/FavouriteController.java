package com.cvgenie.backend.controller;

import com.cvgenie.backend.entity.ApiResponse;
import com.cvgenie.backend.entity.Favourite;
import com.cvgenie.backend.entity.ItemType;
import com.cvgenie.backend.service.FavouriteService;
import com.cvgenie.backend.serviceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class FavouriteController {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private FavouriteService favouriteService;


    @PostMapping("/add-to-favourites/{userId}")
    public ResponseEntity<ApiResponse<Favourite>> addToFavourites(@PathVariable long userId, @RequestBody Favourite favourite){
        logger.info("Received request for Add to Favourites: {}", favourite);
        ApiResponse<Favourite> response = favouriteService.addToFavourites(userId,favourite);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-favourites/{userId}")
    public ResponseEntity<ApiResponse<List<Favourite>>> getFavourites(@PathVariable long userId){
        logger.info("Received request for Get Favourites of User: {}", userId);
        ApiResponse<List<Favourite>> response = favouriteService.getFavourites(userId);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get-favourites-by-id/{id}")
    public ResponseEntity<ApiResponse<Favourite>> getFavouriteById(@PathVariable long id){
        logger.info("Received request for Get Favourite by Id: {}", id);
        ApiResponse<Favourite> response = favouriteService.getFavouriteById(id);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/get-favourites-by-type/{userId}")
    public ResponseEntity<ApiResponse<List<Favourite>>> getFavouriteByType(@PathVariable long userId, @RequestBody ItemType itemType){
        logger.info("Received request for Get Favourites by Type : {}", userId);
        ApiResponse<List<Favourite>> response = favouriteService.getFavouriteByType(userId,itemType);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/remove-favourite/{userId}")
    public ResponseEntity<ApiResponse<Favourite>> removeFavourite(@PathVariable long userId, @RequestParam long itemId){
        logger.info("Received request for remove Favourite: {}", itemId);
        ApiResponse<Favourite> response = favouriteService.removeFavourite(userId,itemId);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
