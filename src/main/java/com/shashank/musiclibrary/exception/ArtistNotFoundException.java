package com.shashank.musiclibrary.exception;

public class ArtistNotFoundException  extends RuntimeException {
    public ArtistNotFoundException(String message) {
        super(message);
    }
}
