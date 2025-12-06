package com.cvgenie.backend.service;

import com.cvgenie.backend.entity.ApiResponse;
import com.cvgenie.backend.entity.Favourite;
import com.cvgenie.backend.entity.ItemType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteService {
    ApiResponse<Favourite> addToFavourites(long userId, Favourite favourite);
    ApiResponse<Favourite> removeFavourite(long userId, long itemId);
    ApiResponse<List<Favourite>> getFavourites(long userId);
    ApiResponse<Favourite> getFavouriteById(long favouriteId);
    ApiResponse<Favourite> getFavouriteByUserIdAndItemId(long userId, long itemId);
    ApiResponse<List<Favourite>> getFavouriteByType(long userId, ItemType itemType);

}
