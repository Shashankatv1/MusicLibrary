package com.shashank.musiclibrary.controller;

import com.shashank.musiclibrary.DTO.FavoriteDTO;
import com.shashank.musiclibrary.exception.ForbiddenAccessException;
import com.shashank.musiclibrary.exception.ResourceNotFoundException;
import com.shashank.musiclibrary.exception.UnauthorizedAccessException;
import com.shashank.musiclibrary.model.Favorite;
import com.shashank.musiclibrary.myutil.ApiResponse;
import com.shashank.musiclibrary.service.FavoriteService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/{category}")
    public ResponseEntity<ApiResponse<List<Favorite>>> getFavorites(@PathVariable String category) {
        try {
            List<Favorite> favorites = favoriteService.getFavoritesByCategory(category);
            return new ResponseEntity<>(new ApiResponse<>(200, favorites, "Favorites retrieved successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Invalid category provided"), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(404, null, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(500, null, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-favorite")
    public ResponseEntity<ApiResponse<FavoriteDTO>> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        try {
            // Add the favorite based on the provided DTO
            FavoriteDTO createdFavorite = favoriteService.addFavorite(favoriteDTO);

            // Return the response with success status
            return new ResponseEntity<>(new ApiResponse<>(201, createdFavorite, "Favorite added successfully."), HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (ForbiddenAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access"), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(404, null, "Resource Doesn't Exist"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove-favorite/{id}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(@PathVariable UUID id) {
        try {
            boolean isRemoved = favoriteService.removeFavorite(id);
            if (isRemoved) {
                return new ResponseEntity<>(new ApiResponse<>(200, null, "Favorite removed successfully"), HttpStatus.OK);
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