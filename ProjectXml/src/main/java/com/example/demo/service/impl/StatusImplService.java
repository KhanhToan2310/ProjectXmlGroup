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

import com.example.demo.model.Status;
import com.example.demo.service.StatusService;


@Service
public class StatusImplService implements StatusService{

	private static String XML_FILE_NAME = "status.xml";
	@Override
	public List<Status> ReadListStatus() {
		List<Status> listStatus = new ArrayList<>();
		Status status = null;
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
						if ("status".equals(reader.getLocalName())) {
							status = new Status();
							status.setId(reader.getAttributeValue(0));
						}

						break;

					case XMLStreamConstants.CHARACTERS:
						tagContent = reader.getText().trim();
						break;

					case XMLStreamConstants.END_ELEMENT:
						switch (reader.getLocalName()) {
						case "status":
							listStatus.add(status);
							break;
						case "name":
							status.setName(tagContent);
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
		return listStatus;
	}

}
