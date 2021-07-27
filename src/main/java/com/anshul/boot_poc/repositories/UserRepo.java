package com.anshul.boot_poc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anshul.boot_poc.entities.UserInfo;

public interface UserRepo extends JpaRepository<UserInfo, Long> {
	
	public boolean existsByToken(String token);
	
	public UserInfo getOneByEmail(String email);
	
	public UserInfo getOneByUniqueName(String uniqueName);
	
	public UserInfo getOneByToken(String token);
}
