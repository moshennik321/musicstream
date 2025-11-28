package com.example.musicstreaming.controller;

import com.example.musicstreaming.model.Playlist;
import com.example.musicstreaming.model.Track;
import com.example.musicstreaming.repository.PlaylistRepository;
import com.example.musicstreaming.repository.TrackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistRepository repo;
    private final TrackRepository trackRepository; // Добавьте эту зависимость

    // Обновите конструктор
    public PlaylistController(PlaylistRepository repo, TrackRepository trackRepository) {
        this.repo = repo;
        this.trackRepository = trackRepository;
    }

    // Статистика плейлиста
    @GetMapping("/{id}/stats")
    public String getPlaylistStats(@PathVariable Long id) {
        Optional<Playlist> playlistOpt = repo.findById(id);

        if (playlistOpt.isEmpty()) {
            return "Playlist not found";
        }

        Playlist playlist = playlistOpt.get();
        int trackCount = playlist.getTracks().size();
        int totalDuration = playlist.getTracks().stream()
                .mapToInt(track -> track.getDuration() != null ? track.getDuration() : 0)
                .sum();

        // Форматируем время (минуты:секунды)
        int minutes = totalDuration / 60;
        int seconds = totalDuration % 60;
        String formattedTime = String.format("%d:%02d", minutes, seconds);

        return String.format(
                "Playlist: %s | Tracks: %d | Total duration: %s (%d seconds)",
                playlist.getName(), trackCount, formattedTime, totalDuration
        );
    }

    // Добавьте метод для добавления треков в плейлист
    @PostMapping("/{playlistId}/tracks")
    public ResponseEntity<Playlist> addTracksToPlaylist(
            @PathVariable Long playlistId,
            @RequestBody List<Long> trackIds) {

        Optional<Playlist> playlistOpt = repo.findById(playlistId);
        if (playlistOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Playlist playlist = playlistOpt.get();
        List<Track> tracks = trackRepository.findAllById(trackIds);

        // Добавляем треки в плейлист
        playlist.getTracks().addAll(tracks);
        Playlist updatedPlaylist = repo.save(playlist);

        return ResponseEntity.ok(updatedPlaylist);
    }

    // Ваши существующие методы остаются без изменений
    @PostMapping
    public Playlist create(@RequestBody Playlist playlist) {
        return repo.save(playlist);
    }

    @GetMapping
    public List<Playlist> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> update(@PathVariable Long id, @RequestBody Playlist updated) {
        return repo.findById(id)
                .map(pl -> {
                    pl.setName(updated.getName());
                    pl.setUser(updated.getUser());
                    pl.setTracks(updated.getTracks());
                    return ResponseEntity.ok(repo.save(pl));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}