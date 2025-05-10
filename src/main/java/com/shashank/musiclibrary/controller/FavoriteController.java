package com.shashank.musiclibrary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shashank.musiclibrary.exception.CategoryNotFoundException;
import com.shashank.musiclibrary.model.Favorite;
import com.shashank.musiclibrary.service.FavoriteService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/users/{userId}/{category}/{entityId}")
    public ResponseEntity<Favorite> getFavorite(
            @PathVariable UUID userId,
            @PathVariable String category,
            @PathVariable UUID entityId) {

        try {
            Optional<Favorite> favorite = favoriteService.getFavoriteByCategory(userId, category, entityId);
            return favorite.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("add-favorite")
    public ResponseEntity<Favorite> addFavorite(@RequestBody Favorite favorite) {
        if (favorite == null) {
            return ResponseEntity.badRequest().build();
        }
        Favorite createdFavorite = favoriteService.addFavorite(favorite);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFavorite);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable UUID favoriteId) {
        if (favoriteId == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean isRemoved = favoriteService.removeFavorite(favoriteId);
        return isRemoved ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}