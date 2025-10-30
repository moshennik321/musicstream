package com.example.musicstreaming.repository;

import com.example.musicstreaming.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
