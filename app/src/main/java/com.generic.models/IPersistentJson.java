package com.generic.models;

public interface IPersistentJson {
	String getId();
	
	void save(String file);
}
