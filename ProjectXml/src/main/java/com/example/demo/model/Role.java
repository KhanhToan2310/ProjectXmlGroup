package com.example.demo.model;

public class Role {
	private String id;
	private String Name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	@Override
	public String toString() {
		return "Role [id=" + id + ", Name=" + Name + "]";
	}
	
}
