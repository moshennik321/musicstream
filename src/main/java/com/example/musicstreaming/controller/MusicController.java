package com.example.musicstreaming.controller;

import com.example.musicstreaming.dto.AlbumRequest;
import com.example.musicstreaming.dto.TopTracksRequest;
import com.example.musicstreaming.model.Album;
import com.example.musicstreaming.model.Playlist;
import com.example.musicstreaming.model.Track;
import com.example.musicstreaming.service.MusicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @PostMapping("/albums/with-tracks")
    public Album createAlbumWithTracks(@RequestBody AlbumRequest request) {
        return musicService.createAlbumWithTracks(request.getAlbum(), request.getTracks());
    }

    @GetMapping("/tracks/genre/{genre}")
    public List<Track> getTracksByGenre(@PathVariable String genre) {
        return musicService.findTracksByGenre(genre);
    }

    @GetMapping("/artists/{id}/stats")
    public Map<String, Object> getArtistStats(@PathVariable Long id) {
        return musicService.getArtistStats(id);
    }

    @PostMapping("/playlists/{playlistId}/tracks/{trackId}")
    public Playlist addTrackToPlaylist(@PathVariable Long playlistId, @PathVariable Long trackId) {
        return musicService.addTrackToPlaylist(playlistId, trackId);
    }

    @PostMapping("/playlists/top-tracks")
    public Playlist createTopTracksPlaylist(@RequestBody TopTracksRequest request) {
        return musicService.createTopTracksPlaylist(
                request.getArtistId(),
                request.getPlaylistName(),
                request.getLimit()
        );
    }
}