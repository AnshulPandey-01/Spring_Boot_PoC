package com.anshul.boot_poc.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anshul.boot_poc.entities.PlaylistsSongs;

public interface PlaylistsSongsRepo extends JpaRepository<PlaylistsSongs, Long> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from playlists_songs where playlist_id = :playlistId", nativeQuery = true)
	public void deletePlaylistsSongs(@Param("playlistId") long playlistId);
	
	@Query(value = "select s.name, s.unique_name from song s inner join playlists_songs pl on s.song_id = pl.song_id where pl.playlist_id = :playlistId", nativeQuery = true)
	public List<Object[]> getPlaylistSongs(@Param("playlistId") long playlistId);
	
}
