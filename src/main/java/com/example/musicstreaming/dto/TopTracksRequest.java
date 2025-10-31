package com.example.musicstreaming.dto;

public class TopTracksRequest {
    private Long artistId;
    private String playlistName;
    private int limit = 10;

    public Long getArtistId() { return artistId; }
    public void setArtistId(Long artistId) { this.artistId = artistId; }

    public String getPlaylistName() { return playlistName; }
    public void setPlaylistName(String playlistName) { this.playlistName = playlistName; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
}