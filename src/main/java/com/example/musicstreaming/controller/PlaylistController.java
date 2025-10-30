package com.example.musicstreaming.controller;

import com.example.musicstreaming.model.Playlist;
import com.example.musicstreaming.repository.PlaylistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistRepository repo;

    public PlaylistController(PlaylistRepository repo) {
        this.repo = repo;
    }

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
