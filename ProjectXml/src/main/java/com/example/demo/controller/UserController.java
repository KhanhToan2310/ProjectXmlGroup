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

	@Autowired
	private PostService postService;
	
	@Autowired
	private AccountService accountService;
	
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
		 
		 List<Post> _listpost = listPostSave;
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
		 
		 
		 
		 for (Post po : listPostTrend) {
			System.err.println(po);
		}
		 
		 for (Post pos : listPostSave) {
				System.err.println(pos);
			}

		 model.addAttribute("listPost", listPostSave);
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


