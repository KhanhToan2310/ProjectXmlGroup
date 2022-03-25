package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.example.demo.model.Role;
import com.example.demo.service.AccountService;
import com.example.demo.service.PostService;
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
	public String checkLoginSession(HttpServletRequest request, @ModelAttribute("account") Account account)
			throws Exception {
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
	public String loginAccount(HttpServletRequest request, @ModelAttribute("account") Account account, ModelMap model)
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
					return "redirect:/selectPostListU";
			} else {
				model.addAttribute("message", "Username or password incorrect. Please input again !");
			}
		}

		return "USER/sign-in";
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
