package com.shashank.musiclibrary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID artist_id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer grammy;
    @Column(nullable = false)
    private boolean hidden;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Track> tracks;

   public boolean getHidden() {
        return hidden;
   }
}
