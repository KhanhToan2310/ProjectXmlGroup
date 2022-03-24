package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.example.demo.model.Result;
import com.example.demo.service.AccountService;
import com.example.demo.service.PostService;
import com.example.demo.util.AccessUtil;

@Controller
public class UserController {

	@Autowired
	private PostService postService;

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String checkLoginSessionUser(HttpServletRequest request, @ModelAttribute("account") Account account, ModelMap model)
			throws Exception {
//		Result result = AccessUtil.accessPage(request,"3");
//		if (result.isValid()) {
//			return "USER/index";
//		}
//		return result.getPath();
		model.addAttribute("message", "");
		String username = (String) request.getSession().getAttribute("username");
		System.out.println("aaaaaaaaaa " + username);
		if (username != null) {
			List<Account> listAccount = accountService.ReadListAccount();
			for (Account a : listAccount) {
				if (username.equals(a.getUsername())) {
					if (a.getRole() == "1")
						return "redirect:/selectAccountList";
					else if (a.getRole() == "2")
						return "redirect:/selectPostList";
					else
						return "redirect:/selectPostListU";
				}
			}
		}
		return "USER/sign-in";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginAccountUser(HttpServletRequest request, @ModelAttribute("account") Account account,
			ModelMap model) throws Exception {
//		Result result = AccessUtil.login(request, account.getUsername(), account.getPassword());
//		if (result.isValid())
//			return result.getPath();
//		else
//			model.addAttribute("message", "Username or password incorrect. Please input again !");
//		return "USER/sign-in";
		for (Account a : accountService.ReadListAccount()) {
			if(a.getUsername().equalsIgnoreCase(account.getUsername()) && a.getPassword().equalsIgnoreCase(account.getPassword()) ) {
				request.getSession().setAttribute("username", account.getUsername());
				if (a.getRole() == "1")
					return "redirect:/selectAccountList";
				else if (a.getRole() == "2")
					return "redirect:/selectPostList";
				else
					return "redirect:/selectPostListU";
			}
			else {
				model.addAttribute("message", "Username or password incorrect. Please input again !");
			}
		}
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/logout")
	public String logoutUser(HttpServletRequest request, @ModelAttribute("account") Account account,
			ModelMap model) throws Exception {
		request.getSession().removeAttribute("username");
		System.out.println("aaaaaa " + request.getSession().getAttribute("username"));
		return "redirect:/login";
	}

	/**
	 * select Post List User
	 * 
	 * @param model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/selectPostListU")
	public String selectPostListUser(ModelMap model)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

		// read list post from data xml
		// sort list post by date update
		// add list post on model
		List<Post> list = postService.readListPost();
		List<Post> listPostSave = new ArrayList<>();

		sortArray(list);

		for (Post post : list) {
			if ("Y".equalsIgnoreCase(post.getIdisvisible()) && "2".equalsIgnoreCase(post.getStatusid())) {

				Account acc = accountService.findAccount(post.getUserid());
				if ((acc.getId()).equalsIgnoreCase(post.getUserid())) {
					post.setUserid(acc.getFullname());
				}
				listPostSave.add(post);
			}
		}
		model.addAttribute("listPost", listPostSave);

		List<Post> _listpost = new ArrayList<Post>();
		for (Post post1 : listPostSave) {
			_listpost.add(post1);
		}
		List<Post> listPostTrend = new ArrayList<Post>();

		if (_listpost.size() > 3) {
			for (int i = 0; i < 3; i++) {
				Post _post = _listpost.get(0);
				for (Post p : _listpost) {
					if ((p.getLikes().size()) > (_post.getLikes().size())) {
						_post = p;
					}
				}
				_listpost.remove(_post);
				listPostTrend.add(_post);

			}
		} else {
			listPostTrend = _listpost;
		}

		model.addAttribute("listPostTrend", listPostTrend);

		return "USER/index";
	}

	private void sortArray(List<Post> arrayList) {
		if (arrayList != null) {
			Collections.sort(arrayList, new Comparator<Post>() {
				@Override
				public int compare(Post o1, Post o2) {
					return o2.getDateupdate().compareTo(o1.getDateupdate());
				}
			});
		}
	}

	/**
	 * select Post List User
	 * 
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
