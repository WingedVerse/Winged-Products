package com.cvgenie.backend.repository;

import com.cvgenie.backend.entity.Favourite;
import com.cvgenie.backend.entity.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite,Long> {
    List<Favourite> findByUserId(long userId);
    List<Favourite> findByUserIdAndItemType(Long userId, ItemType itemType);
    Favourite findByUserIdAndItemId(Long userId, long itemId);

}
