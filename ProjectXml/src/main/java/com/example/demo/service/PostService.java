package com.example.demo.service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.example.demo.model.Account;
import com.example.demo.model.Post;

public interface PostService {

	List<Post> ReadListPost() throws XMLStreamException, FileNotFoundException, UnsupportedEncodingException;
	
	void UpdatePosts(Post _post) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void WritePost(List<Post> _listPost) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void DeletePosts() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void AddNewPosts(Post _post) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
}
