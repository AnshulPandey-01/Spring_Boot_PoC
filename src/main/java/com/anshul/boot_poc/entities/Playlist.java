package com.anshul.boot_poc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Playlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Playlist_Id")
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(name = "unique_name", unique = true, nullable = false)
	private String uniqueName;
	
	@ManyToOne
	@JoinColumn(name = "User_Id")
	private UserInfo user;
	
	public Playlist() {}
	
	public Playlist(String name, String uniqueName, UserInfo user) {
		this.name = name;
		this.uniqueName = uniqueName;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniquename(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Playlist [id=" + id + ", name=" + name + ", uniqueName=" + uniqueName + ", user=" + user.toString() + "]";
	}
	
}
