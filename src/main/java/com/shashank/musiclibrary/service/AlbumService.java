package com.shashank.musiclibrary.service;

import com.shashank.musiclibrary.DTO.AlbumDTO;
import com.shashank.musiclibrary.model.Album;
import com.shashank.musiclibrary.model.Artist;
import com.shashank.musiclibrary.repository.AlbumRepository;
import com.shashank.musiclibrary.repository.ArtistRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistRepository artistRepository;
    // Create a new album
    public Album createAlbum(AlbumDTO albumDTO) throws BadRequestException {
        // Fetch the artist using artist_id
        Optional<Artist> artistOpt = artistRepository.findById(albumDTO.getArtist_id());

        if (!artistOpt.isPresent()) {
            throw new BadRequestException("Artist not found with the given artist_id");
        }

        // Create the album
        Album album = new Album();
        album.setArtist(artistOpt.get());  // Link the album to the found artist
        album.setArtist_name(albumDTO.getArtist_name());
        album.setYear(albumDTO.getYear());
        album.setHidden(albumDTO.isHidden());

        // Save the album
        return albumRepository.save(album);
    }

    // Get all albums
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    // Get album by ID
    public Optional<Album> getAlbumById(UUID albumId) {
        return albumRepository.findById(albumId);
    }

    // Update album information
    public Album updateAlbum(UUID albumId, Album albumDetails) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isPresent()) {
            Album updatedAlbum = album.get();
            updatedAlbum.setArtist_name(albumDetails.getArtist_name());
            updatedAlbum.setYear(albumDetails.getYear());
            updatedAlbum.setHidden(albumDetails.getHidden());
            return albumRepository.save(updatedAlbum);
        }
        return null;
    }

    // Delete album by ID
    public boolean deleteAlbum(UUID albumId) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isPresent()) {
            albumRepository.delete(album.get());  // Delete the artist
            return true;  // Successfully deleted
        } else {
            return false;  // Artist not found
        }
    }


}