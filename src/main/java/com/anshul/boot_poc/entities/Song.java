package com.anshul.boot_poc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Song {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Song_Id")
	private long id;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	@Column(name = "unique_name", unique = true, nullable = false)
	private String uniqueName;
	
	public Song() {}

	public Song(String name, String uniquename) {
		this.name = name;
		this.uniqueName = uniquename;
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

	public void setUniqueName(String uniquename) {
		this.uniqueName = uniquename;
	}

	@Override
	public String toString() {
		return "Song [id=" + id + ", name=" + name + ", uniqueName=" + uniqueName + "]";
	}
	
}
