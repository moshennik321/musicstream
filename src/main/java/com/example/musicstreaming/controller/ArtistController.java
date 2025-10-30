package com.example.musicstreaming.controller;

import com.example.musicstreaming.model.Artist;
import com.example.musicstreaming.repository.ArtistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {
    private final ArtistRepository repo;

    public ArtistController(ArtistRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Artist create(@RequestBody Artist artist) {
        return repo.save(artist);
    }

    @GetMapping
    public List<Artist> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> update(@PathVariable Long id, @RequestBody Artist updated) {
        return repo.findById(id)
                .map(artist -> {
                    artist.setName(updated.getName());
                    artist.setGenre(updated.getGenre());
                    return ResponseEntity.ok(repo.save(artist));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
