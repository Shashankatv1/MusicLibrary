package com.shashank.musiclibrary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID favorite_id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private UUID item_id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at; // Timestamp of creation

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }


}
