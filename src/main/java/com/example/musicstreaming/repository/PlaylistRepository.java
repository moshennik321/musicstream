package com.example.musicstreaming.repository;

import com.example.musicstreaming.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
