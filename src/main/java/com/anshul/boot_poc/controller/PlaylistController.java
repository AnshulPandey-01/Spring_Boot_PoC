package com.anshul.boot_poc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.anshul.boot_poc.entities.Playlist;
import com.anshul.boot_poc.entities.PlaylistsSongs;
import com.anshul.boot_poc.entities.Song;
import com.anshul.boot_poc.entities.UserInfo;
import com.anshul.boot_poc.helper.RandomString;
import com.anshul.boot_poc.helper.StandardResponse;
import com.anshul.boot_poc.repositories.PlaylistRepo;
import com.anshul.boot_poc.repositories.PlaylistsSongsRepo;
import com.anshul.boot_poc.repositories.SongsRepo;
import com.anshul.boot_poc.repositories.UserRepo;

@RestController
public class PlaylistController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private SongsRepo songsRepo;
	
	@Autowired
	private PlaylistRepo playlistRepo;
	
	@Autowired
	private PlaylistsSongsRepo playlistsSongsRepo;
	
	// Returns true if token doesn't exist
	private boolean checkToken(String token) {
		return !userRepo.existsByToken(token);
	}
	
	private Map<String, Object> responsePlaylist(Playlist playlist){
		List<Object[]> list = playlistsSongsRepo.getPlaylistSongs(playlist.getId());
		
		List<StandardResponse> songsList = new ArrayList<>();
		for(Object[] song : list) {
			StandardResponse sr = new StandardResponse((String)song[0], (String)song[1]);
			songsList.add(sr);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("name", playlist.getName());
		map.put("uniqueName", playlist.getUniqueName());
		map.put("user", new StandardResponse(playlist.getUser().getName(), playlist.getUser().getUniqueName()));
		map.put("songs", songsList);
		
		return map;
	}
	
	@GetMapping("/playlist/allPlaylist")
	public ResponseEntity<List<StandardResponse>> getAllPlaylists(){
		System.out.println("getAllPlaylists...");
		
		List<Playlist> playlists = playlistRepo.findAll();
		
		List<StandardResponse> list = new ArrayList<>();
		for(Playlist p : playlists) {
			StandardResponse sr = new StandardResponse(p.getName(), p.getUniqueName());
			list.add(sr);
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/playlist/{uniqueName}")
	public ResponseEntity<Map<?, ?>> getPlaylist(@RequestHeader("token") String token, @PathVariable("uniqueName") String uniqueName){
		System.out.println("getPlaylist...");
		
		if(checkToken(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Playlist playlist = playlistRepo.getOneByUniqueName(uniqueName);
		
		if(playlist == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> map = responsePlaylist(playlist);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@PostMapping(path = "/playlist/create")
	public ResponseEntity<Map<?, ?>> createPlaylist(@RequestHeader("token") String token, @RequestParam(name = "name") String name, @RequestBody List<String> uniqueNames){
		System.out.println("createPlaylist...");
		
		UserInfo u = userRepo.getOneByToken(token);
		
		if(u == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		List<Song> list;
		try {
			list = songsRepo.findByUniqueNameIn(uniqueNames);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String uniqueName = name.substring(0, 3) + RandomString.getAlphaNumericString(7);
		Playlist playlist = playlistRepo.save(new Playlist(name, uniqueName, u));
		
		List<PlaylistsSongs> songsList = new ArrayList<>();
		for(Song song : list) {
			songsList.add(new PlaylistsSongs(playlist, song));
		}
		playlistsSongsRepo.saveAll(songsList);
		
		Map<String, Object> map = responsePlaylist(playlist);
		return new ResponseEntity<>(map ,HttpStatus.OK);
	}
	
	@PutMapping(path = "/playlist/update/{uniqueName}")
	public ResponseEntity<Map<?, ?>> updatePlaylist(@RequestHeader("token") String token, @PathVariable(name = "uniqueName") String uniqueName, @RequestParam(name = "newName") String newName, @RequestBody List<String> uniqueNames){
		System.out.println("updatePlaylist...");
		
		Playlist p = playlistRepo.getOneByUniqueName(uniqueName);
		
		if(p == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else if(!p.getUser().getToken().equals(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		List<Song> list;
		try {
			list = songsRepo.findByUniqueNameIn(uniqueNames);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		List<PlaylistsSongs> songsList = new ArrayList<>();
		for(Song s : list) {
			songsList.add(new PlaylistsSongs(p, s));
		}
		playlistsSongsRepo.saveAll(songsList);
		
		p.setName(newName);
		Playlist finalPlaylist = playlistRepo.save(p);
		
		Map<String, Object> map = responsePlaylist(finalPlaylist);
		return new ResponseEntity<>(map ,HttpStatus.OK);
	}
	
	@DeleteMapping("/playlist/delete")
	public ResponseEntity<String> deletePlaylist(@RequestHeader("token") String token, @RequestParam("uniqueName") String uniqueName){
		System.out.println("deletePlaylist...");
		
		if(checkToken(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Playlist p = playlistRepo.getOneByUniqueName(uniqueName);
		
		if(p == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		playlistsSongsRepo.deletePlaylistsSongs(p.getId());
		playlistRepo.deleteById(p.getId());
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
