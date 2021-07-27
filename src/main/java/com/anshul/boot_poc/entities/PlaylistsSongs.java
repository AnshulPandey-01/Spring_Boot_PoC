package com.anshul.boot_poc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PlaylistsSongs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "Playlist_Id")
	private Playlist playlist;
	
	@ManyToOne
	@JoinColumn(name = "Song_Id")
	private Song song;
	
	public PlaylistsSongs() {}
	
	public PlaylistsSongs(Playlist playlist, Song song) {
		this.playlist = playlist;
		this.song = song;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	@Override
	public String toString() {
		return "PlaylistsSongs [id=" + id + ", playlist=" + playlist + ", song=" + song + "]";
	}
	
}
