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

import com.example.demo.model.Account;
import com.example.demo.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

//	public static void main(String[] args)
//			throws XMLStreamException, FileNotFoundException, UnsupportedEncodingException {
//		AccountServiceImpl n = new AccountServiceImpl();
//
//		Account a = new Account();
////		a.setPassword("duc123");
//
//		a.setIsactive("Y");
//		a.setIsdelete("Y");
//		a.setId("1");
//
//		n.UpdateUser(a);
////		a.setUsername("alo");
//
////		n.ChangePassUser(a.getId(), a.getPassword());
//
////		n.AddNewUser(a);
//
//		List<Account> list = n.ReadListAccount();
//		for (Account account : list) {
//			System.out.println(account);
//		}
//	}

	private static String XML_FILE_NAME = "users.xml";

	@Override
	public List<Account> ReadListAccount()
			throws XMLStreamException, FileNotFoundException, UnsupportedEncodingException {

		FileInputStream fileInPutStream = new FileInputStream(XML_FILE_NAME);
		Reader reader = new java.io.InputStreamReader(fileInPutStream, "utf8");
		BufferedReader br = new BufferedReader(reader);

		XMLInputFactory factory = XMLInputFactory.newInstance();

		XMLEventReader eventReader = factory.createXMLEventReader(br);

		List<Account> listAccount = new ArrayList<>();

		Account a = null;

		while (eventReader.hasNext()) {
			XMLEvent xmlEvent1 = eventReader.nextEvent();

			if (xmlEvent1.isStartElement()) {
				StartElement startElement = xmlEvent1.asStartElement();

				if ("user".equalsIgnoreCase(startElement.getName().getLocalPart())) {
					a = new Account();
				}

				@SuppressWarnings("unchecked")
				Iterator<Attribute> iterator1 = startElement.getAttributes();

				while (iterator1.hasNext()) {
					Attribute attribute = iterator1.next();
					QName name = attribute.getName();
					if ("id".equalsIgnoreCase(name.getLocalPart())) {
						a.setId(attribute.getValue());
					}

				}
				switch (startElement.getName().getLocalPart()) {
				case "username":
					Characters usernameDataEvent = (Characters) eventReader.nextEvent();
					a.setUsername(usernameDataEvent.getData());
					break;

				case "password":
					Characters passwordDataEvent = (Characters) eventReader.nextEvent();
					a.setPassword(passwordDataEvent.getData());
					break;

				case "fullname":
					Characters fullnameDataEvent = (Characters) eventReader.nextEvent();
					a.setFullname(fullnameDataEvent.getData());
					break;

				case "birthday":
					Characters birthdayDataEvent = (Characters) eventReader.nextEvent();
					a.setBirthday(birthdayDataEvent.getData());
					break;

				case "age":
					Characters ageDataEvent = (Characters) eventReader.nextEvent();
					a.setAge(ageDataEvent.getData());
					break;

				case "email":
					Characters emailDataEvent = (Characters) eventReader.nextEvent();
					a.setEmail(emailDataEvent.getData());
					break;

				case "phone":
					Characters phoneDataEvent = (Characters) eventReader.nextEvent();
					a.setPhone(phoneDataEvent.getData());
					break;

				case "role":
					Characters roleDataEvent = (Characters) eventReader.nextEvent();
					a.setRole(roleDataEvent.getData());
					break;
				case "isdelete":
					Characters isdeleteDataEvent = (Characters) eventReader.nextEvent();
					a.setIsdelete(isdeleteDataEvent.getData());
					break;
				case "isactive":
					Characters isactiveDataEvent = (Characters) eventReader.nextEvent();
					a.setIsactive(isactiveDataEvent.getData());
					break;

				}
			}

			if (xmlEvent1.isEndElement()) {
				EndElement endElement = xmlEvent1.asEndElement();

				if ("user".equalsIgnoreCase(endElement.getName().getLocalPart())) {
					listAccount.add(a);
				}

			}
		}
		return listAccount;
	}

	@Override
	public void UpdateUser(Account _acc)
			throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
		// read data and save list for use
		List<Account> _listAccounts = ReadListAccount();

		// read each account for check the same of id's account
		for (Account account : _listAccounts) {

			if (_acc.getId().equalsIgnoreCase(account.getId())) {

					account.setPassword(_acc.getPassword());
				
					account.setFullname(_acc.getFullname());
				
					account.setBirthday(_acc.getBirthday());
				
					account.setAge(_acc.getAge());
				
					account.setEmail(_acc.getEmail());
				
					account.setPhone(_acc.getPhone());
				
					account.setRole(_acc.getRole());
			
					account.setIsdelete(_acc.getIsdelete());

					account.setIsactive(_acc.getIsactive());

			}

		}

		// save data on file XML
		WriteUser(_listAccounts);

	}

	@Override
	public void WriteUser(List<Account> _listAcc)
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

			event = eventFactory.createStartElement("", "", "users");
			writer.add(event);

			for (Account a : _listAcc) {
				event = eventFactory.createStartElement("", "", "user");
				writer.add(event);

				event = eventFactory.createAttribute("id", a.getId());
				writer.add(event);

				event = eventFactory.createStartElement("", "", "username");
				writer.add(event);
				event = eventFactory.createCharacters(a.getUsername());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "username");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "password");
				writer.add(event);
				event = eventFactory.createCharacters(a.getPassword());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "password");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "fullname");
				writer.add(event);
				event = eventFactory.createCharacters(a.getFullname());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "fullname");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "birthday");
				writer.add(event);
				event = eventFactory.createCharacters(a.getBirthday());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "birthday");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "age");
				writer.add(event);
				event = eventFactory.createCharacters(a.getAge());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "age");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "email");
				writer.add(event);
				event = eventFactory.createCharacters(a.getEmail());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "email");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "phone");
				writer.add(event);
				event = eventFactory.createCharacters(a.getPhone());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "phone");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "role");
				writer.add(event);
				event = eventFactory.createCharacters(a.getRole());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "role");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "isdelete");
				writer.add(event);
				event = eventFactory.createCharacters(a.getIsdelete());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "isdelete");
				writer.add(event);

				event = eventFactory.createStartElement("", "", "isactive");
				writer.add(event);
				event = eventFactory.createCharacters(a.getIsactive());
				writer.add(event);
				event = eventFactory.createEndElement("", "", "isactive");
				writer.add(event);

				// end element user
				event = eventFactory.createEndElement("", "", "user");
				writer.add(event);
			}

			event = eventFactory.createEndElement("", "", "users");

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
	public void DeleteUser() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
		// read data and save list for use
		List<Account> _listAccounts = ReadListAccount();
		List<Account> _listAccountsSave = new ArrayList<>();

		// read each account for check isdelete's account if it's "N" add listsave
		for (Account account : _listAccounts) {
			if ("N".equalsIgnoreCase(account.getIsdelete())) {
				_listAccountsSave.add(account);
			}
		}
		// save file data XML
		WriteUser(_listAccountsSave);
	}

	@Override
	public void AddNewUser(Account _acc)
			throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

		// read data and save list for use
		List<Account> _listAccounts = ReadListAccount();

		// for loop i and read each account for check id same if it's same, i++
		int _i = 1;
		for (Account account : _listAccounts) {

			if (!String.valueOf(_i).equalsIgnoreCase(account.getId())
					&& !account.getUsername().equalsIgnoreCase(_acc.getUsername()) && !_acc.getUsername().isEmpty()
					&& !_acc.getPassword().isEmpty()) {
				_acc.setId(String.valueOf(_i));
				break;
			}
			_i++;
		}

		_acc.setId(String.valueOf(_i));
		_listAccounts.add(_acc);

		// save file data XML
		WriteUser(_listAccounts);

	}

	@Override
	public void ChangePassUser(String _idUser, String _passNew)
			throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {

		// read data and save list for use
		List<Account> _listAccounts = ReadListAccount();

		// read each account for check the same of id's account
		for (Account account : _listAccounts) {

			if (_idUser.equalsIgnoreCase(account.getId())) {

				account.setPassword(_passNew);

			}

		}

		// save data on file XML
		WriteUser(_listAccounts);

	}

	@Override
	public Account findAccount(String _id)
			throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
		
		List<Account> list = ReadListAccount();
		Account acc = new Account();
		
		for (Account account : list) {
			if ((account.getId()).equals(_id)) {
				acc = account;
			}
		}
		
		return acc;
	}

}
