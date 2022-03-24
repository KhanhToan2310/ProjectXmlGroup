package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Account;
import com.example.demo.model.Post;
import com.example.demo.model.Role;
import com.example.demo.service.AccountService;
import com.example.demo.service.PostService;
import com.example.demo.util.DateUtil;

@Controller
public class UserController {

	@Autowiredprivate 
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
	
	private void sortArray(List<Post> arrayList) {
		if (arrayList != null) {
		    Collections.sort(arrayList, new Comparator<Post>() {
		        @Override
		        public int compare(Post o1, Post o2) {
		            return o2.getDateupdate().compareTo(o1.getDateupdate()); }
		    });
		} }	
	
	/**
	 * select Post List User
	 * @param id
	 * @param model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/selectPostViewU")
	public String selectPostListUser(ModelMap model, @RequestParam String id)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

		List<Post> list = postService.readListPost();
		Post postSave = new Post();
		
		for (Post post : list) {
			if ((post.getId()).equalsIgnoreCase(id)) {
				Account acc = accountService.findAccount(post.getUserid());
				if ((acc.getId()).equalsIgnoreCase(post.getUserid())) {
					post.setUserid(acc.getFullname());
				}
				postSave = post;
			}
		}
		 
		model.addAttribute("post", postSave);
		
		return "USER/product-detail";
	}
	
}


