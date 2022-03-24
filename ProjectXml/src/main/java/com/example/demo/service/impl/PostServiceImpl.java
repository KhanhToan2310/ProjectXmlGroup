package com.example.demo.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.springframework.stereotype.Service;

import com.example.demo.model.Post;
import com.example.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService {

    private static String XML_FILE_NAME = "posts.xml";

    @Override
    public List<Post> readListPost() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

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
                    Characters titleDataEvent = (Characters) eventReader.nextEvent();
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
    public void updatePosts(Post _post) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

        List<Post> _listPort = readListPost();

        for (Post post : _listPort) {

            if (post.getId().equalsIgnoreCase(_post.getId())) {
                if (!_post.getTitle().isEmpty()) {
                    post.setTitle(_post.getTitle());
                }

                if (!_post.getDesription().isEmpty()) {
                    post.setDesription(_post.getDesription());
                }

                if (!_post.getImg().isEmpty()) {
                    post.setImg(_post.getImg());
                }

                if (!_post.getDatecreate().isEmpty()) {
                    post.setDatecreate(_post.getDatecreate());
                }

                if (!_post.getDateupdate().isEmpty()) {
                    post.setDateupdate(_post.getDateupdate());
                }

                if (!_post.getUserid().isEmpty()) {
                    post.setUserid(_post.getUserid());
                }

                if (!_post.getStatusid().isEmpty()) {
                    post.setStatusid(_post.getStatusid());
                }

                if (!_post.getIsdelete().isEmpty()) {
                    post.setIsdelete(_post.getIsdelete());
                }

                if (!_post.getIdisvisible().isEmpty()) {
                    post.setIdisvisible(_post.getIdisvisible());
                }

                if (!_post.getLikes().isEmpty()) {
                    post.setLikes(_post.getLikes());
                }
            }

        }
        // save on file data XML
        writePost(_listPort);

        for (Post post : _listPort) {
            System.out.println(post);
        }

    }

    @Override
    public void writePost(List<Post> _listPost)
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
    public void deletePosts() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
        // read data and save list for use
        List<Post> _listPost = readListPost();
        List<Post> _listPostSave = new ArrayList<>();

        // read each post for check isdelete's post if it's "N" add listsave
        for (Post post : _listPost) {
            if ("N".equalsIgnoreCase(post.getIsdelete())) {
                _listPostSave.add(post);
            }
        }
        // save file data XML
        writePost(_listPostSave);

    }

    @Override
    public void addNewPosts(Post _post) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

        // read data and save list for use
        List<Post> _listPost = readListPost();

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
        writePost(_listPost);

    }

    @Override
    public Post findPost(String id) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

        List<Post> postList = readListPost();
        Post temp = new Post();
        for (Post post : postList) {

            if (id.equals(post.getId())) {
                temp = post;
            }

        }

        return temp;
    }

}
