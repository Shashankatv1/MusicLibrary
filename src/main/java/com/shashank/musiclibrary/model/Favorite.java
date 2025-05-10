package com.shashank.musiclibrary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "favorite_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) &&
                Objects.equals(category, favorite.category) &&
                Objects.equals(itemId, favorite.itemId) &&
                Objects.equals(userId, favorite.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, itemId, userId);
    }
}