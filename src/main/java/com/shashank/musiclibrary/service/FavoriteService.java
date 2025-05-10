package com.shashank.musiclibrary.service;

import com.shashank.musiclibrary.exception.CategoryNotFoundException;
import com.shashank.musiclibrary.model.Favorite;
import com.shashank.musiclibrary.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public Favorite addFavorite(Favorite favorite) {
        if (favorite == null) {
            throw new IllegalArgumentException("Favorite cannot be null");
        }
        return favoriteRepository.save(favorite);
    }

    public boolean removeFavorite(UUID favoriteId) {
        if (favoriteId == null) {
            throw new IllegalArgumentException("Favorite ID cannot be null");
        }
        if (favoriteRepository.existsById(favoriteId)) {
            favoriteRepository.deleteById(favoriteId);
            return true;
        }
        return false;
    }

    public Optional<Favorite> getFavoriteByCategory(UUID userId, String category, UUID entityId) {
        if (userId == null || category == null || entityId == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        switch (category.toLowerCase()) {
            case "artist":
            case "album":
            case "track":
                return favoriteRepository.findByUserIdAndCategoryAndItemId(userId, category, entityId);
            default:
                throw new CategoryNotFoundException("Invalid category: " + category);
        }
    }
}

