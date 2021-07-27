package com.anshul.boot_poc.helper;

public class StandardResponse {

	private String name;
	
	private String uniqueName;

	public StandardResponse(String name, String uniqueName) {
		this.name = name;
		this.uniqueName = uniqueName;
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

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
	
}
