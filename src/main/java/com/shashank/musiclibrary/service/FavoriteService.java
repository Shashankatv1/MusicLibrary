package com.shashank.musiclibrary.service;

import com.shashank.musiclibrary.DTO.FavoriteDTO;
import com.shashank.musiclibrary.model.Album;
import com.shashank.musiclibrary.model.Artist;
import com.shashank.musiclibrary.model.Favorite;
import com.shashank.musiclibrary.model.Track;
import com.shashank.musiclibrary.repository.AlbumRepository;
import com.shashank.musiclibrary.repository.ArtistRepository;
import com.shashank.musiclibrary.repository.FavoriteRepository;
import com.shashank.musiclibrary.repository.TrackRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TrackRepository trackRepository;

    public FavoriteDTO addFavorite(FavoriteDTO favoriteDTO) throws BadRequestException {
        // Create a Favorite object to save
        Favorite favorite = new Favorite();

        if ("artist".equalsIgnoreCase(favoriteDTO.getCategory())) {
            Artist artist = artistRepository.findById(favoriteDTO.getItemId())
                    .orElseThrow(() -> new BadRequestException("Artist not found"));
            favorite.setCategory("artist");
            favorite.setItem_id(artist.getArtist_id());
            favoriteDTO.setName(artist.getName());  // Setting the artist's name

        } else if ("album".equalsIgnoreCase(favoriteDTO.getCategory())) {
            Album album = albumRepository.findById(favoriteDTO.getItemId())
                    .orElseThrow(() -> new BadRequestException("Album not found"));
            favorite.setCategory("album");
            favorite.setItem_id(album.getAlbum_Id());
            favoriteDTO.setName(album.getArtist_name());  // Setting the album's name

        } else if ("track".equalsIgnoreCase(favoriteDTO.getCategory())) {
            Track track = trackRepository.findById(favoriteDTO.getItemId())
                    .orElseThrow(() -> new BadRequestException("Track not found"));
            favorite.setCategory("track");
            favorite.setItem_id(track.getTrack_id());
            favoriteDTO.setName(track.getName());  // Setting the track's name

        } else {
            throw new BadRequestException("Invalid category. Must be 'artist', 'album', or 'track'.");
        }

        favorite = favoriteRepository.save(favorite);

        favoriteDTO.setFavorite_id(favorite.getFavorite_id());
        favoriteDTO.setCreated_at(favorite.getCreated_at());

        return favoriteDTO;
    }

    public boolean removeFavorite(UUID favoriteId) {
        if (favoriteRepository.existsById(favoriteId)) {
            favoriteRepository.deleteById(favoriteId);
            return true;
        }
        return false;
    }

    public List<Favorite> getFavoritesByCategory(String category) {
        return favoriteRepository.findByCategory(category);
    }


}
