package com.shashank.musiclibrary.controller;

import com.shashank.musiclibrary.DTO.TrackRequest;
import com.shashank.musiclibrary.exception.ForbiddenAccessException;
import com.shashank.musiclibrary.exception.UnauthorizedAccessException;
import com.shashank.musiclibrary.model.Track;
import com.shashank.musiclibrary.myutil.ApiResponse;
import com.shashank.musiclibrary.service.TrackService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Track>>> getAllTracks() {
        try {
            List<Track> tracks = trackService.getAllTracks();
            return new ResponseEntity<>(new ApiResponse<>(200, tracks, "Tracks retrieved successfully"), HttpStatus.OK);
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<Track>>> getTrack(@PathVariable UUID id) {
        try {
            Optional<Track> track = trackService.getTrackById(id);
            if (track.isPresent()) {
                return new ResponseEntity<>(new ApiResponse<>(200, track, "Track retrieved successfully"), HttpStatus.OK);
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
    @PostMapping("/add-track")
    public ResponseEntity<ApiResponse<Track>> createTrack(@RequestBody TrackRequest trackRequest) {
        try {
            Track track = trackService.addTrack(
                    trackRequest.getArtist_id(),
                    trackRequest.getAlbum_id(),
                    trackRequest.getName(),
                    trackRequest.getDuration(),
                    trackRequest.isHidden()
            );
            return new ResponseEntity<>(new ApiResponse<>(201, track, "Track created successfully"), HttpStatus.CREATED);
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

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Track>> updateTrack(@PathVariable UUID id, @RequestBody Track track) {
        try {
            Track updatedTrack = trackService.updateTrack(id, track);
            if (updatedTrack != null) {
                return new ResponseEntity<>(new ApiResponse<>(204, updatedTrack, "Track updated successfully"), HttpStatus.NO_CONTENT);
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
    public ResponseEntity<ApiResponse<Void>> deleteTrack(@PathVariable UUID id) {
        try {
            boolean isDeleted = trackService.deleteTrack(id);
            if (isDeleted) {
                return new ResponseEntity<>(new ApiResponse<>(200, null, "Track deleted successfully"), HttpStatus.OK);
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
