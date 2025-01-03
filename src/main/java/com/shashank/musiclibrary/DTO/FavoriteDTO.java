package com.shashank.musiclibrary.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class FavoriteDTO {

    private UUID favorite_id;
    private String category;
    private UUID itemId;
    private String name;
    private LocalDateTime created_at;
}
