package com.anshul.boot_poc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anshul.boot_poc.entities.Playlist;

public interface PlaylistRepo extends JpaRepository<Playlist, Long> {

	public Playlist getOneByUniqueName(String uniqueName);
	
}
