package com.example.musicstreaming.repository;

import com.example.musicstreaming.model.Track;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    // Найти треки по ID артиста
    @Query("SELECT t FROM Track t WHERE t.artist.id = :artistId")
    List<Track> findByArtistId(@Param("artistId") Long artistId);

    // Выполнить поиск треков по названию
    @Query("SELECT t FROM Track t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Track> searchByTitle(@Param("query") String query);

    // Найти треки по жанру артиста
    @Query("SELECT t FROM Track t WHERE t.artist.genre = :genre")
    List<Track> findByArtistGenre(@Param("genre") String genre);

    // Найти топ треков артиста по продолжительности
    @Query("SELECT t FROM Track t WHERE t.artist.id = :artistId ORDER BY t.duration DESC")
    List<Track> findTopByArtistId(@Param("artistId") Long artistId, Pageable pageable);
}