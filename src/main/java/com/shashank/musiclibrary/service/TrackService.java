package com.shashank.musiclibrary.service;

import com.shashank.musiclibrary.model.Album;
import com.shashank.musiclibrary.model.Artist;
import com.shashank.musiclibrary.model.Track;
import com.shashank.musiclibrary.repository.AlbumRepository;
import com.shashank.musiclibrary.repository.ArtistRepository;
import com.shashank.musiclibrary.repository.TrackRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;
    // Create a new track
    public Track addTrack(UUID artist_id, UUID album_id, String name, int duration, boolean hidden) throws BadRequestException {
        Artist artist = artistRepository.findById(artist_id)
                .orElseThrow(() -> new BadRequestException("Artist not found"));
        Album album = albumRepository.findById(album_id)
                .orElseThrow(() -> new BadRequestException("Album not found"));

        Track track = new Track();
        track.setName(name);  // Set the track name (you need to pass this from the client)
        track.setDuration(duration);  // Set the track duration (you need to pass this as well)
        track.setHidden(hidden);  // Set the hidden flag (if passed)

        track.setArtist(artist);  // Set the associated artist
        track.setAlbum(album);  // Set the associated album

        return trackRepository.save(track);
    }


    // Get all tracks
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    // Get track by ID
    public Optional<Track> getTrackById(UUID trackId) {
        return trackRepository.findById(trackId);
    }

    // Update track information
    public Track updateTrack(UUID trackId, Track trackDetails) {
        Optional<Track> track = trackRepository.findById(trackId);
        if (track.isPresent()) {
            Track updatedTrack = track.get();
            updatedTrack.setName(trackDetails.getName());
            updatedTrack.setDuration(trackDetails.getDuration());
            updatedTrack.setHidden(trackDetails.getHidden());
            return trackRepository.save(updatedTrack);
        }
        return null;
    }

    // Delete track by ID
    public boolean deleteTrack(UUID trackId) {
        if (trackRepository.existsById(trackId)) {
            // Delete the track
            trackRepository.deleteById(trackId);
            return true;  // Successfully deleted
        }
        return false;
    }



}
