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

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

import com.example.demo.DemoApplication;
import com.example.demo.model.Role;
import com.example.demo.service.RoleService;


@Service
public class RoleImplService implements RoleService {

	private static String XML_FILE_NAME = "roles.xml";

	@Override
	public List<Role> ReadListRole() {

		List<Role> listRole = new ArrayList<>();
		Role role = null;
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
						if ("role".equals(reader.getLocalName())) {
							role = new Role();
							role.setId(reader.getAttributeValue(0));
						}

						break;

					case XMLStreamConstants.CHARACTERS:
						tagContent = reader.getText().trim();
						break;

					case XMLStreamConstants.END_ELEMENT:
						switch (reader.getLocalName()) {
						case "role":
							listRole.add(role);
							break;
						case "name":
							role.setName(tagContent);
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
		return listRole;
	}

}
