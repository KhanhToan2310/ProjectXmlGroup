package com.example.demo.util;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.Account;
import com.example.demo.model.Result;
import com.example.demo.service.AccountService;

public class AccessUtil {
	
	@Autowired
	public static AccountService accountService;

	public static HttpServletRequest request;

	public static String getAtributeSession(HttpServletRequest request, String nameAtribute) {
		return (String) request.getAttribute(nameAtribute);
	}

	public static void setAtributeSession(HttpServletRequest request, String nameAtribute, String value) {
		request.getSession().setAttribute(nameAtribute, value);
	}

	public static Result accessPage(HttpServletRequest request, String role) throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {
		Result result = new Result("USER/sign-in",false);
		String username = getAtributeSession(request, "username");
		
		for (Account a : accountService.ReadListAccount()) {
			if(a.getUsername().equalsIgnoreCase(username) && a.getRole().equalsIgnoreCase(role)) {
				result.isValid = true;
			}
		}
		return result;
	}
	
	public static Result login(HttpServletRequest request, String username, String password) throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {
		Result result = new Result("USER/sign-in",false);
		
		for (Account a : accountService.ReadListAccount()) {
			if(a.getUsername().equalsIgnoreCase(username) && a.getPassword().equalsIgnoreCase(password) ) {
				result.isValid = true;
				setAtributeSession(request, "username", username);
				if (a.getRole() == "1")
					result.path = "ADMIN/tablesAccounts";
				else if (a.getRole() == "2")
					result.path = "ADMIN/postList";
				else
					result.path = "USER/index";
			}
		}
		return result;
	}
	 
	public static void logout() {
		request.removeAttribute("username");
	}

}


