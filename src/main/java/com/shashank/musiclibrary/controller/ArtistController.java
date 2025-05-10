package com.shashank.musiclibrary.controller;

import com.shashank.musiclibrary.exception.ArtistNotFoundException;
import com.shashank.musiclibrary.model.Artist;
import com.shashank.musiclibrary.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<Artist> getArtist(@PathVariable UUID artistId) {
        return artistService.getArtistById(artistId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found with id: " + artistId));
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(artistService.createArtist(artist));
    }

    @PutMapping("/{artistId}")
    public ResponseEntity<Artist> updateArtist(
            @PathVariable UUID artistId,
            @RequestBody Artist artist) {
        return ResponseEntity.ok(artistService.updateArtist(artistId, artist));
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteArtist(@PathVariable UUID artistId) {
        artistService.deleteArtist(artistId);
        return ResponseEntity.noContent().build();
    }
}