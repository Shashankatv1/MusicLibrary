package com.shashank.musiclibrary.service;

import com.shashank.musiclibrary.model.Artist;
import com.shashank.musiclibrary.repository.ArtistRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    public Artist createArtist(Artist artist) throws BadRequestException {
        if (artist.getName() == null || artist.getName().isEmpty()) {
            throw new BadRequestException("Artist name cannot be null or empty");
        }
        return artistRepository.save(artist);
    }

    // Get all artists
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    // Get artist by ID
    public Optional<Artist> getArtistById(UUID artistId) {
        return artistRepository.findById(artistId);
    }

    // Update artist information
    public Artist updateArtist(UUID artistId, Artist artistDetails) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            Artist updatedArtist = artist.get();
            updatedArtist.setName(artistDetails.getName());
            updatedArtist.setGrammy(artistDetails.getGrammy());
            updatedArtist.setHidden(artistDetails.getHidden());
            return artistRepository.save(updatedArtist);
        }
        return null;
    }

    // Delete artist by ID
    public boolean deleteArtist(UUID artistId) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            artistRepository.delete(artist.get());  // Delete the artist
            return true;  // Successfully deleted
        } else {
            return false;  // Artist not found
        }
    }


}
