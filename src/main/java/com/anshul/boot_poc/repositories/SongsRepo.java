package com.anshul.boot_poc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anshul.boot_poc.entities.Song;

public interface SongsRepo extends JpaRepository<Song, Long> {
	
	public boolean existsByName(String name);
	
	public Song getOneByName(String name);
	
	public Song getOneByUniqueName(String uniqueName);
	
	public List<Song> findByUniqueNameIn(List<String> uniqueNames);
	
}
