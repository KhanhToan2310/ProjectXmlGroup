package com.example.demo.service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.example.demo.model.Account;

public interface AccountService {

	List<Account> ReadListAccount() throws XMLStreamException, FileNotFoundException, UnsupportedEncodingException;
	
	void UpdateUser(Account _acc) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void AddNewUser(Account _acc) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void DeleteUser() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void WriteUser(List<Account> _listAcc) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
	void ChangePassUser(String _idUser, String _passNew) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException;
	
}
