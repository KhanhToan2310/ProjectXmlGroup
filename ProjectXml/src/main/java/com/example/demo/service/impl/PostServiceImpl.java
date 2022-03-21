package com.example.demo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springframework.stereotype.Service;

import com.example.demo.model.Post;
import com.example.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService{

	private static String XML_FILE_NAME = "posts.xml";
	
	@Override
	public List<Post> ReadListPost() {
		
		List<Post> listPost = new ArrayList<>();
		Post post = null;
		String tagContent = null;

		File inputFile = new File(XML_FILE_NAME);
		InputStream is;
		try {
			is = new FileInputStream(inputFile);

			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory.createXMLStreamReader(is);

			try {
				while (reader.hasNext()) {
					int event = reader.next();

					switch (event) {
					case XMLStreamConstants.START_ELEMENT:
						if ("post".equals(reader.getLocalName())) {
							post = new Post();
							post.setId(reader.getAttributeValue(0));
						}

						break;

					case XMLStreamConstants.CHARACTERS:
						tagContent = reader.getText().trim();
						break;

					case XMLStreamConstants.END_ELEMENT:
						switch (reader.getLocalName()) {
						case "post":
							listPost.add(post);
							break;
						case "title":
							post.setTitle(tagContent);
							break;
						}
						break;
					}
				}
			} catch (NumberFormatException | XMLStreamException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
		return listPost;
		
	}

	
	
}
