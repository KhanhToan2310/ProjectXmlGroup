package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Account;
import com.example.demo.model.Post;
import com.example.demo.model.Result;
import com.example.demo.service.AccountService;
import com.example.demo.service.PostService;
import com.example.demo.util.AccessUtil;
import com.example.demo.service.RoleService;
import com.example.demo.util.DateUtil;

@Controller
public class UserController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private PostService postService;

	@Autowired
	private RoleService roleService;
	
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
			} else {
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
	 * register
	 * 
	 * @param model
	 * @param HttpServletRequest
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/register")
	public String addAccount(ModelMap model)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

		List<Account> list = accountService.ReadListAccount();
		
		model.addAttribute("accounts",list);

		return "USER/sign-up";
	}

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
	

		String userNameLog = (String) request.getSession().getAttribute("username");

		List<Account> ListAccount = accountService.ReadListAccount();
		String userPostedId = "";
		for (Account account : ListAccount) {
			if (account.getUsername().equalsIgnoreCase(userNameLog)) {
				userPostedId = account.getId();

			}
		}

		for (Post post : listPostSave) {
			for (String likes : post.getLikes()) {
				if (likes.equals(userPostedId)) {
					post.setFlg("Y");
					break;
				} else {
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
		
		model.addAttribute("userPostedId", userPostedId);
		model.addAttribute("listPostTrend", listPostTrend);

		return "USER/index";
	}


	/**
	 * sort array funtion
	 * 
	 * @param List
	 * @return void
	 */

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

	/**
	 * select Post List Of User
	 * 
	 * @param HttpServletRequest
	 * @param model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/selectPostViewOfU")
	public String selectPostListOfUser(ModelMap model, HttpServletRequest request)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

		List<Post> list = postService.readListPost();
		List<Post> listPostSave = new ArrayList<>();

		// sort list post by date update
		sortArray(list);

		// get username on session
		String userNameLog = (String) request.getSession().getAttribute("username");

		// find id's user by usernameLog
		List<Account> ListAccount = accountService.ReadListAccount();
		// user id
		String userPostedId = "";
		for (Account account : ListAccount) {
			if (account.getUsername().equalsIgnoreCase(userNameLog)) {
				userPostedId = account.getId();

			}
		}

		// get posts have condition
		for (Post post : list) {
			if (post.getUserid().equalsIgnoreCase(userPostedId) && "Y".equalsIgnoreCase(post.getIdisvisible())
					&& "2".equalsIgnoreCase(post.getStatusid())) {

				Account acc = accountService.findAccount(post.getUserid());
				if ((acc.getId()).equalsIgnoreCase(post.getUserid())) {
					post.setUserid(acc.getFullname());
				}
				listPostSave.add(post);
			}
		}

		// set likes
		for (Post post : listPostSave) {
			for (String likes : post.getLikes()) {
				if (likes.equals(userPostedId)) {
					post.setFlg("Y");
					break;
				} else {
					post.setFlg("N");
				}

			}
		}
		model.addAttribute("listPost", listPostSave);

		List<Post> _listpost = new ArrayList<Post>();
		for (Post post1 : listPostSave) {
			_listpost.add(post1);
		}

		model.addAttribute("userPostedId", userPostedId);

		return "USER/myBlog";
	}

	/**
	 * select Account View User
	 * 
	 * @param HttpServletRequest
	 * @param model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/selectAccountU")
	public String selectAccountviewU(ModelMap model, HttpServletRequest request)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

		// get username on session
		String userNameLog = (String) request.getSession().getAttribute("username");

		// find id's user by usernameLog
		List<Account> ListAccount = accountService.ReadListAccount();
		// user id
		String userPostedId = "";
		for (Account account : ListAccount) {
			if (account.getUsername().equalsIgnoreCase(userNameLog)) {
				userPostedId = account.getId();

			}
		}

		Account account = accountService.findAccount(userPostedId);
		Map<String, String> selectRoleMap = selectRoleMap();

		model.addAttribute("selectRoleMap", selectRoleMap);
		model.addAttribute("account", account);

		return "USER/accountForm";
	}

	/**
	 * update Post Like Ajax
	 * 
	 * @param id
	 * @param valCheckbox
	 * @param value
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePostLikeAjax")
	public String updatePostLike(@RequestParam String id, @RequestParam String idUser, @RequestParam String value)
			throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

		System.err.println("alo: " + id + ", " + idUser + ", " + value);

		List<Post> list = postService.readListPost();

		for (Post post : list) {
			if (post.getId().equalsIgnoreCase(id)) {
				List<String> likes = post.getLikes();

				for (String like : likes) {
					// remove on likes
					if (value.equalsIgnoreCase("N")) {

						if (!like.equalsIgnoreCase(idUser)) {
							post.setFlg("Y");
							likes.add(idUser);
							post.setLikes(likes);
							System.err.println("alo: " + post);
							postService.updatePosts(post);
						}

					}
					// add on likes
					else {
						if (like.equalsIgnoreCase(idUser)) {
							post.setFlg("N");
							likes.remove(like);
							post.setLikes(likes);
							System.err.println("alo: " + post);
							postService.updatePosts(post);
						}
					}
				}
			}

		}

		return "redirect:/selectPostListU";
	}
	
	
	/**
     * update Account  User
     * 
     * @param id
     * @param phone
     * @param email
     * @param age
     * @param birthday
     * @param fullname
     * @return null
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @ResponseBody
    @RequestMapping(value = "/updateAccountFindU")
    public String updateAccountFind(@RequestParam String id, @RequestParam String phone, @RequestParam String email,
            @RequestParam String age, @RequestParam String birthday,
            @RequestParam(name = "fullname", required = false) String fullname)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        List<Account> resultList = accountService.ReadListAccount();

        for (Account account : resultList) {
            if (account.getId().equalsIgnoreCase(id)) {
                account.setAge(age);
                account.setBirthday(birthday);
                account.setEmail(email);
                account.setFullname(fullname);
                account.setPhone(phone);

                accountService.UpdateUser(account);

            }
        }

        return null;
    }

    /**
     * update Account Password User
     * 
     * @param id
     * @param phone
     * @return null
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @ResponseBody
    @RequestMapping(value = "/updateAccountPasswordU")
    public String updateAccountPassword(@RequestParam String id, @RequestParam String newPassword)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        List<Account> resultList = accountService.ReadListAccount();

        for (Account account : resultList) {
            if (account.getId().equalsIgnoreCase(id)) {
                account.setPassword(newPassword);

                accountService.UpdateUser(account);
            }
        }

        return "USER/accountForm";
    }
    
    /**
     * register
     * 
     * @param username
     * @param phone
     * @param email
     * @param age
     * @param birthday
     * @param fullname
     * @return null
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @ResponseBody
    @RequestMapping(value = "/registered")
    public String registertd(@RequestParam String username, @RequestParam String phone, @RequestParam String email,
            @RequestParam String age, @RequestParam String birthday, @RequestParam String password, RedirectAttributes redirAttrs,
            @RequestParam(name = "fullname", required = false) String fullname)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        List<Account> resultList = accountService.ReadListAccount();
        Account a = new Account();
        boolean flag = false;

        for (Account account : resultList) {
            if (account.getUsername().equalsIgnoreCase(username)) {
            	flag = false;
            	break;
            }else {
            	flag = true;
			}
            
        }
        
        if (flag == true) {
        	a.setUsername(username);
        	a.setPassword(password);
        	a.setPhone(phone);
        	a.setEmail(email);
        	a.setAge(age);
        	a.setBirthday(birthday);
        	a.setFullname(fullname);
        	a.setRole("3");
        	a.setIsactive("Y");
        	a.setIsdelete("N");
        	
        	System.err.println("alo: "+a);
        	
            accountService.AddNewUser(a);
           
            
            
            
		}
        
        return null;
    }
	
	
	/**
     * select Role Map
     * 
     * @return Map<String, String>
     */
    private Map<String, String> selectRoleMap() {
        Map<String, String> map = new HashMap<String, String>();
        List<Role> roles = roleService.ReadListRole();

        for (Role role : roles) {
            map.put(role.getId(), role.getName());
        }

        return map;
    }

}
