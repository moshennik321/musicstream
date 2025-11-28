package com.example.musicstreaming.controller;
import org.springframework.data.domain.PageRequest;
import com.example.musicstreaming.model.Track;
import com.example.musicstreaming.model.Artist;
import com.example.musicstreaming.model.Album;
import com.example.musicstreaming.repository.TrackRepository;
import com.example.musicstreaming.repository.ArtistRepository;
import com.example.musicstreaming.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    @Autowired
    public TrackController(TrackRepository trackRepository,
                           ArtistRepository artistRepository,
                           AlbumRepository albumRepository) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

    @PostMapping
    public Track create(@RequestBody Track track) {
        // Если передан artist с id, загружаем полного артиста из базы
        if (track.getArtist() != null && track.getArtist().getId() != null) {
            Artist managedArtist = artistRepository.findById(track.getArtist().getId())
                    .orElseThrow(() -> new RuntimeException("Artist not found"));
            track.setArtist(managedArtist);
        }

        // То же самое для album
        if (track.getAlbum() != null && track.getAlbum().getId() != null) {
            Album managedAlbum = albumRepository.findById(track.getAlbum().getId())
                    .orElseThrow(() -> new RuntimeException("Album not found"));
            track.setAlbum(managedAlbum);
        }

        return trackRepository.save(track);
    }

    // Остальные методы остаются без изменений
    @GetMapping
    public List<Track> getAll() {
        return trackRepository.findAll();
    }
    // В этом методе реализован поиск треков по части названия
    @GetMapping("/search")
    public List<Track> searchTracks(@RequestParam String query) {
        return trackRepository.searchByTitle(query);
    }
    @GetMapping("/by-artist/{artistId}")
    public List<Track> getTracksByArtist(@PathVariable Long artistId) {
        return trackRepository.findByArtistId(artistId);
    }
    // Треки по жанру
    @GetMapping("/by-genre/{genre}")
    public List<Track> getTracksByGenre(@PathVariable String genre) {
        return trackRepository.findByArtistGenre(genre);
    }

    // Топ треков артиста
    @GetMapping("/top-by-artist/{artistId}")
    public List<Track> getTopTracksByArtist(@PathVariable Long artistId,
                                            @RequestParam(defaultValue = "5") int limit) {
        return trackRepository.findTopByArtistId(artistId, PageRequest.of(0, limit));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Track> getById(@PathVariable Long id) {
        return trackRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Track> update(@PathVariable Long id, @RequestBody Track updated) {
        return trackRepository.findById(id)
                .map(track -> {
                    track.setTitle(updated.getTitle());
                    track.setDuration(updated.getDuration());
                    track.setArtist(updated.getArtist());
                    track.setAlbum(updated.getAlbum());
                    return ResponseEntity.ok(trackRepository.save(track));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        trackRepository.deleteById(id);
    }
}