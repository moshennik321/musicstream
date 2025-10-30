package com.example.musicstreaming.repository;

import com.example.musicstreaming.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
