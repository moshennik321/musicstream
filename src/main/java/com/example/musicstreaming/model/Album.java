package com.example.musicstreaming.model;

import jakarta.persistence.*;

@Entity
@Table(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer releaseYear;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist; // ← СВЯЗЬ С АРТИСТОМ

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Artist getArtist() { return artist; }
    public void setArtist(Artist artist) { this.artist = artist; }
}