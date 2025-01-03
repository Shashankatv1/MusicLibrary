package com.shashank.musiclibrary.controller;

import com.shashank.musiclibrary.DTO.AlbumDTO;
import com.shashank.musiclibrary.exception.ForbiddenAccessException;
import com.shashank.musiclibrary.exception.UnauthorizedAccessException;
import com.shashank.musiclibrary.model.Album;
import com.shashank.musiclibrary.myutil.ApiResponse;
import com.shashank.musiclibrary.service.AlbumService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Album>>> getAllAlbums() {
        try {
            List<Album> albums = albumService.getAllAlbums();
            return new ResponseEntity<>(new ApiResponse<>(200, albums, "Albums retrieved successfully"), HttpStatus.OK);
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<Album>>> getAlbum(@PathVariable UUID id) {
        try {
            Optional<Album> album = albumService.getAlbumById(id);
            if (album.isPresent()) {
                return new ResponseEntity<>(new ApiResponse<>(200, album, "Album retrieved successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(404, null, "Resource Doesn't Exist"), HttpStatus.NOT_FOUND);
            }
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (ForbiddenAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access"), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/add-album")
    public ResponseEntity<ApiResponse<?>> addAlbum(@RequestBody AlbumDTO albumDTO) {
        try {

            albumService.createAlbum(albumDTO);

            ApiResponse<?> response = new ApiResponse<>(201, "Album created successfully", null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            ApiResponse<?> response = new ApiResponse<>(400,  "Bad Request", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Album>> updateAlbum(@PathVariable UUID id, @RequestBody Album album) {
        try {
            Album updatedAlbum = albumService.updateAlbum(id, album);
            if (updatedAlbum != null) {
                return new ResponseEntity<>(new ApiResponse<>(204, updatedAlbum, "Album updated successfully"), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(404, null, "Resource Doesn't Exist"), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<ApiResponse<Void>> deleteAlbum(@PathVariable UUID id) {
        try {
            boolean isDeleted = albumService.deleteAlbum(id);
            if (isDeleted) {
                return new ResponseEntity<>(new ApiResponse<>(200, null, "Album deleted successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(404, null, "Resource Doesn't Exist"), HttpStatus.NOT_FOUND);
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
