package com.example.musicstreaming.model;

import jakarta.persistence.*;

@Entity
@Table(name = "track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist; // ← СВЯЗЬ С АРТИСТОМ

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Artist getArtist() { return artist; }
    public void setArtist(Artist artist) { this.artist = artist; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
}