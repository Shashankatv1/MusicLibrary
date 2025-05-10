package com.shashank.musiclibrary.repository;

import com.shashank.musiclibrary.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    Optional<Favorite> findByUserIdAndCategoryAndItemId(UUID userId, String category, UUID itemId);

    // Additional useful queries
    List<Favorite> findByUserId(UUID userId);
    List<Favorite> findByUserIdAndCategory(UUID userId, String category);
    boolean existsByUserIdAndCategoryAndItemId(UUID userId, String category, UUID itemId);
}
