package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Account;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;

@Controller
public class PostController {

	@Autowired
	private PostService postService;

	@RequestMapping(value = "/create-post", method = RequestMethod.GET)
	public String getCreatePostPage(@ModelAttribute("post") Post post, @RequestParam String postId) {
		if (postId == null || postId == "")
			return "USER/post-edit-create";
		else {
			return "redirect:/edit-post";
		}
	}

	@RequestMapping(value = "/edit-post", method = RequestMethod.GET)
	public String getEditPostPage(HttpServletRequest request, @ModelAttribute("post") Post post,
			@RequestParam String postId, ModelMap model)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {
		Account account = (Account) request.getSession().getAttribute("account");
		for (Post p : postService.readListPost()) {
			if (p.getId().equalsIgnoreCase(postId.trim()) && post.getUserid().equalsIgnoreCase(account.getId())) {
				model.addAttribute("post", p);
				return "USER/post-edit-create";
			}
		}
		return "redirect:/selectPostListU";
	}
}
