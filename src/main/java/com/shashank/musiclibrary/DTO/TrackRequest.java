package com.shashank.musiclibrary.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TrackRequest {
    private UUID artist_id;
    private UUID album_id;
    private String name;
    private int duration;
    private boolean hidden;

}

