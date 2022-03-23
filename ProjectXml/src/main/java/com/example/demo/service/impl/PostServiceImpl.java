package com.example.demo.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.*;

import org.springframework.stereotype.Service;

import com.example.demo.model.Account;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService {

//	public static void main(String[] args)
//			throws XMLStreamException, FileNotFoundException, UnsupportedEncodingException {
//		PostServiceImpl n = new PostServiceImpl();
//
//		Post p = new Post();
//		p.setId("1");
//		p.setDatecreate("1");
//		p.setDateupdate("1");
//		p.setDesription("1");
//		p.setIdisvisible("1");
//		p.setImg("1");
//		p.setIsdelete("1");
//		List<String> s = new ArrayList<String>();
//		s.add("1");
//		s.add("2");
//		
//		p.setLikes(s);
//		p.setStatusid("1");
//		p.setTitle("1");
//		p.setUserid("1");
//		n.AddNewPosts(p);
//		
//		
//
////		n.WritePost(n.ReadListPost());
//
//		List<Post> list = n.ReadListPost();
//		for (Post post : list) {
//			System.out.println(post);
//		}
//
//	}

	private static String XML_FILE_NAME = "posts.xml";

	@Override
	public List<Post> ReadListPost() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

		FileInputStream fileInPutStream = new FileInputStream(XML_FILE_NAME);
		Reader reader = new java.io.InputStreamReader(fileInPutStream, "utf8");
		BufferedReader br = new BufferedReader(reader);

		XMLInputFactory factory = XMLInputFactory.newInstance();

		XMLEventReader eventReader = factory.createXMLEventReader(br);

		List<Post> ListPost = new ArrayList<>();
		List<String> likes = new ArrayList<>();

		String _temp = null;
		Post p = null;

		while (eventReader.hasNext()) {
			XMLEvent xmlEvent1 = eventReader.nextEvent();

			if (xmlEvent1.isStartElement()) {
				StartElement startElement = xmlEvent1.asStartElement();

				if ("post".equalsIgnoreCase(startElement.getName().getLocalPart())) {
					p = new Post();
				}

				if ("likes".equalsIgnoreCase(startElement.getName().getLocalPart())) {
					likes = new ArrayList<>();
				}

				@SuppressWarnings("unchecked")
				Iterator<Attribute> iterator1 = startElement.getAttributes();

				while (iterator1.hasNext()) {
					Attribute attribute = iterator1.next();
					QName name = attribute.getName();
					if ("id".equalsIgnoreCase(name.getLocalPart())) {
						p.setId(attribute.getValue());
					}
					if ("likes".equalsIgnoreCase(name.getLocalPart())) {
						ListPost = new ArrayList<>();
					}
					if ("userlikeid".equalsIgnoreCase(name.getLocalPart())) {
						likes = new ArrayList<>();
					}

				}
				switch (startElement.getName().getLocalPart()) {
				case "title":
					Characters titleDataEvent =  (Characters) eventReader.nextEvent();
					p.setTitle(titleDataEvent.getData().trim());
					break;

				case "desription":
					Characters desriptionDataEvent = (Characters) eventReader.nextEvent();
					p.setDesription(desriptionDataEvent.getData().trim());
					break;

				case "img":
					Characters imgDataEvent = (Characters) eventReader.nextEvent();
					p.setImg(imgDataEvent.getData().trim());
					break;

				case "datecreate":
					Characters datecreateDataEvent = (Characters) eventReader.nextEvent();
					p.setDatecreate(datecreateDataEvent.getData().trim());
					break;

				case "dateupdate":
					Characters dateupdateDataEvent = (Characters) eventReader.nextEvent();
					p.setDateupdate(dateupdateDataEvent.getData().trim());
					break;

				case "userid":
					Characters userpostedidDataEvent = (Characters) eventReader.nextEvent();
					p.setUserid(userpostedidDataEvent.getData().trim());
					break;

				case "statusid":
					Characters statusidDataEvent = (Characters) eventReader.nextEvent();
					p.setStatusid(statusidDataEvent.getData().trim());
					break;
				case "userlikeid":
					Characters useridlikedDataEvent = (Characters) eventReader.nextEvent();
					_temp = useridlikedDataEvent.getData();
					break;
				case "isdelete":
					Characters isdeleteDataEvent = (Characters) eventReader.nextEvent();
					p.setIsdelete(isdeleteDataEvent.getData());
					break;
				case "isvisible":
					Characters isvisibleDataEvent = (Characters) eventReader.nextEvent();
					p.setIdisvisible(isvisibleDataEvent.getData());
					break;

				}
			}

			if (xmlEvent1.isEndElement()) {
				EndElement endElement = xmlEvent1.asEndElement();

				if ("post".equalsIgnoreCase(endElement.getName().getLocalPart())) {
					ListPost.add(p);
				}
				if ("userlikeid".equalsIgnoreCase(endElement.getName().getLocalPart())) {
					likes.add(_temp);

				}
				if ("likes".equalsIgnoreCase(endElement.getName().getLocalPart())) {
					p.setLikes(likes);
				}
			}
		}
		return ListPost;

	}

	@Override
	public void UpdatePosts(Post _post) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

		List<Post> _listPort = ReadListPost();

		for (Post post : _listPort) {

			if (post.getId().equalsIgnoreCase(_post.getId())) {
				if (!_post.getTitle().isEmpty()) {
					post.setTitle(_post.getTitle());
				} else if (!_post.getDesription().isEmpty()) {
					post.setDesription(_post.getDesription());
				} else if (!_post.getImg().isEmpty()) {
					post.setImg(_post.getImg());
				} else if (!_post.getDatecreate().isEmpty()) {
					post.setDatecreate(_post.getDatecreate());
				} else if (!_post.getDateupdate().isEmpty()) {
					post.setDateupdate(_post.getDateupdate());
				} else if (!_post.getUserid().isEmpty()) {
					post.setUserid(_post.getUserid());
				} else if (!_post.getStatusid().isEmpty()) {
					post.setStatusid(_post.getStatusid());
				} else if (!_post.getIsdelete().isEmpty()) {
					post.setIsdelete(_post.getIsdelete());
				} else if (!_post.getIdisvisible().isEmpty()) {
					post.setIdisvisible(_post.getIdisvisible());
					;
				} else if (!_post.getLikes().isEmpty()) {
					post.setLikes(_post.getLikes());
				}
			}

		}
		// save on file data XML
		WritePost(_listPort);

		for (Post post : _listPort) {
			System.out.println(post);
		}

	}

	@Override
	public void WritePost(List<Post> _listPost)
			throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

		FileInputStream fileInPutStream = new FileInputStream(XML_FILE_NAME);
		Reader reader = new java.io.InputStreamReader(fileInPutStream, "utf8");
		BufferedReader br = new BufferedReader(reader);

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		try {
			XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(XML_FILE_NAME));

			XMLEvent event = eventFactory.createStartDocument();
			writer.add(event);

			event = eventFactory.createStartElement("", "", "posts");
			writer.add(event);

			for (Post a : _listPost) {
				event = eventFactory.createStartElement("", "", "post");
				writer.add(event);

				event = eventFactory.createAttribute("id", a.getId());
				writer.add(event);

				event = eventFactory.createStartElement("", "", "title");
				writer.add(event);
				event = eventFactory.createCharacters(a.getTitle());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "title");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "desription");
				writer.add(event);
				event = eventFactory.createCharacters(a.getDesription());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "desription");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "img");
				writer.add(event);
				event = eventFactory.createCharacters(a.getImg());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "img");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "datecreate");
				writer.add(event);
				event = eventFactory.createCharacters(a.getDatecreate());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "datecreate");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "dateupdate");
				writer.add(event);
				event = eventFactory.createCharacters(a.getDateupdate());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "dateupdate");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "userid");
				writer.add(event);
				event = eventFactory.createCharacters(a.getUserid());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "userid");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "statusid");
				writer.add(event);
				event = eventFactory.createCharacters(a.getStatusid());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "statusid");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "likes");
				writer.add(event);

				List<String> _templike = a.getLikes();

				if (_templike != null) {
					for (int i = 0; i < _templike.size(); i++) {
						event = eventFactory.createStartElement("", "", "userlikeid");
						writer.add(event);
						event = eventFactory.createCharacters(_templike.get(i));
						writer.add(event);
						event = eventFactory.createEndElement("", "", "userlikeid");
						writer.add(event);
					}
				}

				event = eventFactory.createEndElement("", "", "likes");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "isdelete");
				writer.add(event);
				event = eventFactory.createCharacters(a.getIsdelete());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "isdelete");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "isvisible");
				writer.add(event);
				event = eventFactory.createCharacters(a.getIdisvisible());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "isvisible");
				writer.add(event);

				// end element user
				event = eventFactory.createEndElement("", "", "post");
				writer.add(event);
			}

			event = eventFactory.createEndElement("", "", "posts");
			writer.add(event);

			writer.flush();
			writer.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void DeletePosts() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
		// read data and save list for use
		List<Post> _listPost = ReadListPost();
		List<Post> _listPostSave = new ArrayList<>();

		// read each post for check isdelete's post if it's "N" add listsave
		for (Post post : _listPost) {
			if ("N".equalsIgnoreCase(post.getIsdelete())) {
				_listPostSave.add(post);
			}
		}
		// save file data XML
		WritePost(_listPostSave);

	}

	@Override
	public void AddNewPosts(Post _post) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

		// read data and save list for use
		List<Post> _listPost = ReadListPost();

		// for loop i and read each post for check id same if it's same, i++
		int _i = 1;
		for (Post post : _listPost) {

			if (!String.valueOf(_i).equalsIgnoreCase(post.getId())) {
				_post.setId(String.valueOf(_i));
				break;
			}
			_i++;
		}

		_post.setId(String.valueOf(_i));
		_listPost.add(_post);

//		 save file data XML
		WritePost(_listPost);

	}

}
