package com.anshul.boot_poc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anshul.boot_poc.entities.UserInfo;
import com.anshul.boot_poc.helper.RandomString;
import com.anshul.boot_poc.helper.StandardResponse;
import com.anshul.boot_poc.repositories.UserRepo;

@RestController
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping(path = "/user/allUsers")
	public ResponseEntity<List<StandardResponse>> getAllUsers(){
		System.out.println("getAllUsers...");
		
		List<UserInfo> users = userRepo.findAll();
		
		if(users == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<StandardResponse> list = new ArrayList<>();
		for(UserInfo u : users){
			StandardResponse sr = new StandardResponse(u.getName(), u.getUniqueName());
			list.add(sr);
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@PostMapping(path = "/user/signup", consumes = {"application/json"})
	public ResponseEntity<Map<String, String>> addUser(@RequestBody UserInfo user) {
		System.out.println("addUser...");
		
		user.setUniqueName(user.getName().substring(0, 3) + RandomString.getAlphaNumericString(7));
		UserInfo u = userRepo.save(user);
		
		Map<String, String> map = new HashMap<>();
		map.put("name", u.getName());
		map.put("email", u.getEmail());
		map.put("password", u.getPassword());
		map.put("uniqueName", u.getUniqueName());
		
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/user/login", consumes = {"application/json"})
	public ResponseEntity<Map<String, String>> login(@RequestBody UserInfo user){
		System.out.println("login...");
		
		UserInfo userInfo = userRepo.getOneByEmail(user.getEmail());
		
		if(userInfo == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if(!userInfo.getPassword().equals(user.getPassword())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		userInfo.setToken(RandomString.getAlphaNumericString(7) + userInfo.getUniqueName().substring(7, 10));
		UserInfo u = userRepo.save(userInfo);
		
		Map<String, String> map = new HashMap<>();
		map.put("name", u.getName());
		map.put("email", u.getEmail());
		map.put("password", u.getPassword());
		map.put("uniqueName", u.getUniqueName());
		map.put("token", u.getToken());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("token", u.getToken());
		
		return new ResponseEntity<>(map, headers, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/user/logout")
	public ResponseEntity<String> logout(@RequestParam("uniqueName") String uniqueName, HttpServletRequest request){
		System.out.println("logout...");
		System.out.println(request.getParameter("uniqueName"));
		UserInfo u = userRepo.getOneByUniqueName(uniqueName);
		
		if(u == null) {
			return new ResponseEntity<>("No user with this unique name.", HttpStatus.NOT_FOUND);
		}
		
		if(u.getToken() != null) {
			u.setToken(null);
			userRepo.save(u);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("token", null);
		
		return new ResponseEntity<>("log out successfull", headers, HttpStatus.OK);
	}
	
}
