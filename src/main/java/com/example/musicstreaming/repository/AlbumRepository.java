package com.example.musicstreaming.repository;
import java.util.stream.Collectors;
import com.example.musicstreaming.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Упрощенный метод - Spring Data сам поймет
    List<Album> findByArtistId(Long artistId);

    // Новые альбомы
    @Query("SELECT a FROM Album a ORDER BY a.id DESC")
    List<Album> findNewAlbums(org.springframework.data.domain.Pageable pageable);
}