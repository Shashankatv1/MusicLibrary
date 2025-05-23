package com.shashank.musiclibrary.repository;//package com.shashank.MusicLibrary.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shashank.musiclibrary.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    User findByEmail(String email);
}
