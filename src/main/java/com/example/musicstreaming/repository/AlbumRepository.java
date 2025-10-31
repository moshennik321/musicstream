package com.example.musicstreaming.repository;

import com.example.musicstreaming.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("SELECT a FROM Album a WHERE a.artist.id = :artistId")
    List<Album> findByArtistId(@Param("artistId") Long artistId);
}