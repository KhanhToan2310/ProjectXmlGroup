package com.example.demo.service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.example.demo.model.Post;

public interface PostService {

	List<Post> readListPost() throws XMLStreamException, FileNotFoundException, UnsupportedEncodingException;
	
	void updatePosts(Post _post) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void writePost(List<Post> _listPost) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void deletePosts() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void addNewPosts(Post _post) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	Post findPost(String id) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
}
