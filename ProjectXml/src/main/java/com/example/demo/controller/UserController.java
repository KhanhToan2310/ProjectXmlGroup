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

import javax.servlet.http.HttpServletRequest;
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
	
	/**
	 * select Post List User
	 * 
	 * @param model
	 * @param HttpServletRequest
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/selectPostListU")
	public String selectPostListU(ModelMap model, HttpServletRequest request)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

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
		 
		 String userNameLog = (String) request.getAttribute("userName");
		 
		 System.err.println("alo: "+ userNameLog);
		 for (Post post : listPostSave) {
			for (String likes : post.getLikes()) {
				if (likes.equals(request.getAttribute("userName"))) {
					post.setFlg("Y");
					break;
				}else {
					post.setFlg("N");
				}
				
			}
		}
		 model.addAttribute("listPost", listPostSave);
		 
		 List<Post> _listpost = new ArrayList<Post>();
		 for (Post post1 : listPostSave) {
			_listpost.add(post1);
		}
		 List<Post> listPostTrend = new ArrayList<Post>();
		 
		 if(_listpost.size()>3) {
			 for(int i=0 ; i<3; i++) {
				 Post _post = _listpost.get(0);
				 for(Post p : _listpost) {
					 if ((p.getLikes().size()) > (_post.getLikes().size())) {
						_post = p;
					}
				 }
				 _listpost.remove(_post);
				 listPostTrend.add(_post);
				 
			 }
		 }else {
			listPostTrend =  _listpost;
		}
		 
		
		 
		 model.addAttribute("listPostTrend", listPostTrend);
		 
		return "USER/index";
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


