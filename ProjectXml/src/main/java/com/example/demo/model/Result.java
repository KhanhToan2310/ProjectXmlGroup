package com.example.demo.model;

public class Result {
	public String path;
	public boolean isValid;

	public Result() {
		// TODO Auto-generated constructor stub
	}

	public Result(String path, boolean isValid) {
		this.path = path;
		this.isValid = isValid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}