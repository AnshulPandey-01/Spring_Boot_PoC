package com.anshul.boot_poc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anshul.boot_poc.entities.Song;
import com.anshul.boot_poc.helper.RandomString;
import com.anshul.boot_poc.helper.StandardResponse;
import com.anshul.boot_poc.repositories.SongsRepo;
import com.anshul.boot_poc.repositories.UserRepo;

@RestController
public class SongController {
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private SongsRepo songsRepo;
	
	// Returns true if token doesn't exist
	private boolean checkToken(String token) {
		return !userRepo.existsByToken(token);
	}
	
	@GetMapping("/song/allSongs")
	public ResponseEntity<List<StandardResponse>> getAllSongs(){
		System.out.println("getAllSongs...");
		
		List<Song> songs = songsRepo.findAll();
		
		List<StandardResponse> songsList = new ArrayList<>();
		for(Song s : songs) {
			StandardResponse sr = new StandardResponse(s.getName(), s.getUniqueName());
			songsList.add(sr);
		}
		
		return new ResponseEntity<>(songsList, HttpStatus.OK);
	}
	
	@GetMapping("/song/{uniqueName}")
	public ResponseEntity<StandardResponse> getSong(@RequestHeader("token") String token, @PathVariable("uniqueName") String uniqueName) {
		System.out.println("getSong...");
		
		if(checkToken(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Song s = songsRepo.getOneByUniqueName(uniqueName);
		
		if(s == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		StandardResponse sr = new StandardResponse(s.getName(), s.getUniqueName());
		return new ResponseEntity<>(sr, HttpStatus.OK);
	}
	
	@PostMapping("/song/addSong")
	public ResponseEntity<StandardResponse> addSong(@RequestHeader("token") String token, @RequestParam(name = "name") String name){
		System.out.println("addSong...");
		
		if(checkToken(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}else if(songsRepo.existsByName(name)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		String uniqueName = name.substring(0, 3) + RandomString.getAlphaNumericString(7);
		Song s = songsRepo.save(new Song(name, uniqueName));
		
		StandardResponse sr = new StandardResponse(s.getName(), s.getUniqueName());
		return new ResponseEntity<>(sr, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/song/update", consumes = {"application/json"})
	public ResponseEntity<StandardResponse> updateSong(@RequestHeader("token") String token, @RequestParam(name = "uniqueName") String uniqueName, @RequestBody Song song){
		System.out.println("updateSong...");
		
		if(checkToken(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Song repoSong = songsRepo.getOneByUniqueName(uniqueName);
		
		if(repoSong == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		repoSong.setName(song.getName());
		Song s = songsRepo.save(repoSong);
		
		StandardResponse sr = new StandardResponse(s.getName(), s.getUniqueName());
		return new ResponseEntity<>(sr, HttpStatus.OK);
	}
	
	@DeleteMapping("/song/delete")
	public ResponseEntity<String> deleteSong(@RequestHeader("token") String token, @RequestParam(name = "uniqueName") String uniqueName){
		System.out.println("deleteSong...");
		
		if(checkToken(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Song s = songsRepo.getOneByUniqueName(uniqueName);
		
		if(s == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		try {
			songsRepo.delete(s);
		}catch(Exception e) {
			return new ResponseEntity<>("Song is part of atleast one Playlist.", HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
