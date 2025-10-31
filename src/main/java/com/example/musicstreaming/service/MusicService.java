package com.example.musicstreaming.service;

import com.example.musicstreaming.model.Album;
import com.example.musicstreaming.model.Artist;
import com.example.musicstreaming.model.Playlist;
import com.example.musicstreaming.model.Track;
import com.example.musicstreaming.repository.AlbumRepository;
import com.example.musicstreaming.repository.ArtistRepository;
import com.example.musicstreaming.repository.PlaylistRepository;
import com.example.musicstreaming.repository.TrackRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MusicService {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;

    public MusicService(ArtistRepository artistRepository,
                        AlbumRepository albumRepository,
                        TrackRepository trackRepository,
                        PlaylistRepository playlistRepository) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.playlistRepository = playlistRepository;
    }

    // 1. Создание альбома с треками (транзакция)
    public Album createAlbumWithTracks(Album album, List<Track> tracks) {
        Album savedAlbum = albumRepository.save(album);
        for (Track track : tracks) {
            track.setAlbum(savedAlbum);
            trackRepository.save(track);
        }
        return savedAlbum;
    }

    // 2. Поиск треков по жанру
    public List<Track> findTracksByGenre(String genre) {
        return trackRepository.findByArtistGenre(genre);
    }

    // 3. Получение статистики артиста
    public Map<String, Object> getArtistStats(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        List<Album> albums = albumRepository.findByArtistId(artistId);
        List<Track> tracks = trackRepository.findByArtistId(artistId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("artist", artist.getName());
        stats.put("albumCount", albums.size());
        stats.put("trackCount", tracks.size());
        stats.put("totalDuration", tracks.stream().mapToInt(Track::getDuration).sum());

        return stats;
    }

    // 4. Добавление трека в плейлист
    public Playlist addTrackToPlaylist(Long playlistId, Long trackId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        playlist.getTracks().add(track);
        return playlistRepository.save(playlist);
    }

    // 5. Создание плейлиста из топ треков артиста
    public Playlist createTopTracksPlaylist(Long artistId, String playlistName, int limit) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        List<Track> topTracks = trackRepository.findTopByArtistId(artistId, PageRequest.of(0, limit));

        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist.setTracks(new ArrayList<>(topTracks));

        return playlistRepository.save(playlist);
    }
}