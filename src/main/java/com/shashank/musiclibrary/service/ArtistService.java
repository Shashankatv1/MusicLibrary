package com.shashank.musiclibrary.service;

import com.shashank.musiclibrary.exception.ArtistNotFoundException;
import com.shashank.musiclibrary.model.Artist;
import com.shashank.musiclibrary.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist createArtist(Artist artist) {
        if (artist == null) {
            throw new IllegalArgumentException("Artist cannot be null");
        }
        return artistRepository.save(artist);
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> getArtistById(UUID artistId) {
        return artistRepository.findById(artistId);
    }

    public Artist updateArtist(UUID artistId, Artist artistDetails) {
        Artist existingArtist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found with id: " + artistId));

        existingArtist.setName(artistDetails.getName());
        existingArtist.setGrammy(artistDetails.getGrammy());
        existingArtist.setHidden(artistDetails.getHidden());

        return artistRepository.save(existingArtist);
    }

    public void deleteArtist(UUID artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found with id: " + artistId));
        artistRepository.delete(artist);
    }
}