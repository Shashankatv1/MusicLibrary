package com.shashank.musiclibrary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


    @Entity
    @Getter
    @Setter
    @Table(name = "albums")
    public class Album {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID album_Id; // Unique identifier for the album

        private String artist_name;

        @ManyToOne
        @JoinColumn(name = "artist_id", referencedColumnName = "artist_id", nullable = false)
        private Artist artist;

        @Column(nullable = false)
        private Integer year;

        @Column(nullable = false)
        private Boolean hidden;
        @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Track> tracks;
    }
