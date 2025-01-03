package com.shashank.musiclibrary.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PasswordRequest {
    private UUID userId;
    private String oldPassword;
    private String newPassword;

}
