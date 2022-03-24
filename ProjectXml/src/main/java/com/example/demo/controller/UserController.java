package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.demo.model.Account;
import com.example.demo.service.AccountService;

@Controller
public class UserController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String checkLoginSession(HttpServletRequest request,@ModelAttribute("account") Account account) throws Exception {
		String username = (String) request.getSession().getAttribute("username");
		if (username != null) {
			List<Account> listAccount = accountService.ReadListAccount();
			for (Account a : listAccount) {
				if (username.equals(a.getUsername())) {
					if (a.getRole() == "1")
						return "ADMIN/tablesAccounts";
					else if (a.getRole() == "2")
						return "ADMIN/postList";
					else
						return "USER/index";
				}
			}
		}
		return "USER/sign-in";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginAccount(HttpServletRequest request,@ModelAttribute("account") Account account, ModelMap model)
			throws Exception {
		for (Account a : accountService.ReadListAccount()) {
			if (a.getUsername().equalsIgnoreCase(account.getUsername().trim())
					&& a.getPassword().equalsIgnoreCase(account.getPassword().trim())) {
				request.getSession().setAttribute("username", account.getUsername().trim());
				if (a.getRole() == "1")
					return "ADMIN/tablesAccounts";
				else if (a.getRole() == "2")
					return "ADMIN/postList";
				else
					return "USER/index";
			} else {
				model.addAttribute("message", "Username or password incorrect. Please input again !");
			}
		}

		return "USER/sign-in";
	}
}
