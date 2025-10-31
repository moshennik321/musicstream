package com.example.musicstreaming.dto;

import com.example.musicstreaming.model.Album;
import com.example.musicstreaming.model.Track;
import java.util.List;

public class AlbumRequest {
    private Album album;
    private List<Track> tracks;

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    public List<Track> getTracks() { return tracks; }
    public void setTracks(List<Track> tracks) { this.tracks = tracks; }
}