package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Account;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import com.example.demo.service.impl.PostServiceImpl;

@Controller
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private PostServiceImpl postServiceImpl;

	@RequestMapping(value = "/create-post", method = RequestMethod.GET)
	public String getCreatePostPage(@ModelAttribute("post") Post post) {
			return "USER/post-create";
	}

	@RequestMapping(value = "/edit-post", method = RequestMethod.GET)
	public String getEditPostPage(HttpServletRequest request, @ModelAttribute("post") Post post, @RequestParam String id ,ModelMap model)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {
		Account account = (Account) request.getSession().getAttribute("account");
		for (Post p : postService.readListPost()) {
			if (p.getId().equalsIgnoreCase(id.trim()) && post.getUserid().equalsIgnoreCase(account.getId())) {
				model.addAttribute("post", p);
				return "USER/post-edit";
			}
		}
		return "redirect:/selectPostListU";
	}
	
	@RequestMapping(value = "/registerPost", method = RequestMethod.POST)
	public String registerPost(HttpServletRequest request, @ModelAttribute("post") Post post) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
		LocalDate localDate = LocalDate.now();
		Account account = (Account) request.getSession().getAttribute("account");
	
		post.setDatecreate(dtf.format(localDate));
		post.setDateupdate(dtf.format(localDate));
		post.setUserid(account.getId());
		post.setStatusid("1");
		post.setIsdelete("N");
		post.setIdisvisible("Y");
		try {
			postServiceImpl.addNewPosts(post);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/selectPostViewOfU";
	}
	
	@RequestMapping(value = "/updatePost", method = RequestMethod.POST)
	public String updatePost(HttpServletRequest request, @ModelAttribute("post") Post post, @RequestParam String id) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
		LocalDate localDate = LocalDate.now();
		post.setDateupdate(dtf.format(localDate));
		try {
			postServiceImpl.updatePosts(post);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/selectPostViewOfU";
	}
	
}
