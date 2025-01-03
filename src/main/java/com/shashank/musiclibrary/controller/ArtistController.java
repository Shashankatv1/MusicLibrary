package com.shashank.musiclibrary.controller;

import com.shashank.musiclibrary.exception.ForbiddenAccessException;
import com.shashank.musiclibrary.exception.UnauthorizedAccessException;
import com.shashank.musiclibrary.model.Artist;
import com.shashank.musiclibrary.myutil.ApiResponse;
import com.shashank.musiclibrary.service.ArtistService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Artist>>> getAllArtists() {
        try {
            List<Artist> artists = artistService.getAllArtists();
            return new ResponseEntity<>(new ApiResponse<>(200, artists, "Artists retrieved successfully"), HttpStatus.OK);
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<Artist>>> getArtist(@PathVariable UUID id) {
        try {
            Optional<Artist> artist = artistService.getArtistById(id);
            if (artist.isPresent()) {
                return new ResponseEntity<>(new ApiResponse<>(200, artist, "Artist retrieved successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(404, null, "Artist not found"), HttpStatus.NOT_FOUND);
            }
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (ForbiddenAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access"), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/artist")
    public ResponseEntity<ApiResponse<?>> addArtist(@RequestBody Artist artist) {
        try {
            Artist createdArtist = artistService.createArtist(artist);
            return new ResponseEntity<>(new ApiResponse<>(201,  "Artist created successfully",null), HttpStatus.CREATED);
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Artist>> updateArtist(@PathVariable UUID id, @RequestBody Artist artist) {
        try {
            Artist updatedArtist = artistService.updateArtist(id, artist);
            if (updatedArtist != null) {
                return new ResponseEntity<>(new ApiResponse<>(200, updatedArtist, "Artist updated successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(404, null, "Artist not found"), HttpStatus.NOT_FOUND);
            }
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (ForbiddenAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access"), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArtist(@PathVariable UUID id) {
        try {
            boolean isDeleted = artistService.deleteArtist(id);
            if (isDeleted) {
                return new ResponseEntity<>(new ApiResponse<>(200, null, "Artist deleted successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(404, null, "Artist not found"), HttpStatus.NOT_FOUND);
            }
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (ForbiddenAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access"), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
    }
}

