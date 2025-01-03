package com.shashank.musiclibrary.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AlbumDTO {

        private UUID artist_id;
        private String artist_name;
        private int year;
        private boolean hidden;
}
