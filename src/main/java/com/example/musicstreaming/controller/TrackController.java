package com.example.musicstreaming.controller;

import com.example.musicstreaming.model.Track;
import com.example.musicstreaming.repository.TrackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {
    private final TrackRepository repo;

    public TrackController(TrackRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Track create(@RequestBody Track track) {
        return repo.save(track);
    }

    @GetMapping
    public List<Track> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Track> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Track> update(@PathVariable Long id, @RequestBody Track updated) {
        return repo.findById(id)
                .map(track -> {
                    track.setTitle(updated.getTitle());
                    track.setDuration(updated.getDuration());
                    track.setArtist(updated.getArtist());
                    track.setAlbum(updated.getAlbum());
                    return ResponseEntity.ok(repo.save(track));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
