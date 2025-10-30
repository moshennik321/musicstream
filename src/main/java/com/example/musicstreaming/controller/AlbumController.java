package com.example.musicstreaming.controller;

import com.example.musicstreaming.model.Album;
import com.example.musicstreaming.repository.AlbumRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final AlbumRepository repo;

    public AlbumController(AlbumRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Album create(@RequestBody Album album) {
        return repo.save(album);
    }

    @GetMapping
    public List<Album> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> update(@PathVariable Long id, @RequestBody Album updated) {
        return repo.findById(id)
                .map(album -> {
                    album.setTitle(updated.getTitle());
                    album.setReleaseYear(updated.getReleaseYear());
                    album.setArtist(updated.getArtist());
                    return ResponseEntity.ok(repo.save(album));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
