package com.shashank.musiclibrary.model;//package com.shashank.MusicLibrary.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID user_id;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime created_at;

    public enum Role {
        ADMIN,
        EDITOR,
        VIEWER
    }
    @PrePersist
    public void prePersist() {
        if (created_at == null) {
            created_at = LocalDateTime.now(); // Set current date and time if createdAt is not provided
        }
    }
}
