package com.example.demo.controller;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Account;
import com.example.demo.model.Post;
import com.example.demo.model.Role;
import com.example.demo.model.Status;
import com.example.demo.service.AccountService;
import com.example.demo.service.PostService;
import com.example.demo.service.RoleService;
import com.example.demo.service.StatusService;
import com.example.demo.util.DateUtil;

@Controller
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PostService postService;

    @Autowired
    private StatusService statusService;

    /**
     * select Post List
     * 
     * @param model
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/selectPostList")
    public String selectPostList(ModelMap model) throws Exception {

        // select list post
        try {
            List<Post> resultList = postService.ReadListPost();

            for (Post post : resultList) {
                post.setDatecreate(DateUtil.getDateFormat("yyyy-MM-dd"));
            }

            model.addAttribute("resultList", resultList);
        } catch (FileNotFoundException | UnsupportedEncodingException | XMLStreamException e) {
            throw new Exception("Lỗi");
        }

        model.addAttribute("statusMap", selectStatusMap());
        return "ADMIN/examples/postList";
    }

    /**
     * update Post
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
    @RequestMapping(value = "/updatePost")
    public String updatePost(@RequestParam String id, @RequestParam String valCheckbox, @RequestParam String value)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        List<Post> resultList = postService.ReadListPost();
        Post temp = new Post();

        for (Post post : resultList) {
            if (id.equals(post.getId())) {
                temp = post;

                if ("delete".equals(value)) {
                    temp.setIsdelete(valCheckbox);
                } else {
                    temp.setIdisvisible(valCheckbox);
                }

                postService.UpdatePosts(temp);

            }
        }

        return valCheckbox;
    }

    // selectAccountView
    @RequestMapping("/selectAccountList")
    public String selectAccountList(ModelMap model)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        Map<String, String> selectRoleMap = selectRoleMap();
        List<Account> accountList = accountService.ReadListAccount();
        List<Account> list = new ArrayList<Account>();

        for (Account account : accountList) {
            if (!"1".equals(account.getRole())) {
                list.add(account);
            }
        }

        List<Role> listRole = roleService.ReadListRole();
        List<Role> listRole2 = new ArrayList<>();

        for (Role role : listRole) {
            if (!"1".equalsIgnoreCase(role.getId())) {
                listRole2.add(role);
            }
        }

        model.addAttribute("listRole", listRole2);
        model.addAttribute("listAccount", list);
        model.addAttribute("selectRoleMap", selectRoleMap);

        return "ADMIN/examples/tablesAccounts";
    }

    private Map<String, String> selectRoleMap() {
        Map<String, String> map = new HashMap<String, String>();
        List<Role> roles = roleService.ReadListRole();

        for (Role role : roles) {
            map.put(role.getId(), role.getName());
        }

        return map;
    }

    @RequestMapping(value = { "/selectAccountList" }, method = RequestMethod.POST)
	public String updateAccount(ModelMap model, HttpServletRequest request) {

		try {
			
			String idAcc = request.getParameter("idacc");
			String isdelete = request.getParameter("isdelete");
			String isactive = request.getParameter("isactive");
			String role = request.getParameter("roleacc");
			
			List<Account> listAccount = accountService.ReadListAccount();
			Account a = new Account();
			for (Account account : listAccount) {
				if ((account.getId()).equalsIgnoreCase(idAcc)) {
					a = account;
					
					a.setId(idAcc);
				    a.setRole(role);
				    if ("on".equalsIgnoreCase(isdelete)) {
						a.setIsdelete("Y");
					}else {
						a.setIsdelete("N");
					}
				    if ("on".equalsIgnoreCase(isactive)) {
						a.setIsactive("Y");
					}else {
						a.setIsactive("N");
					}
				    
				    
				    accountService.UpdateUser(a);
				}
			}
			
		    
			
		} catch (Exception e) {
		}
    	
		return "redirect:/selectAccountList";

	}

    private Map<String, String> selectStatusMap() {
        Map<String, String> map = new HashMap<String, String>();
        List<Status> statusList = statusService.ReadListStatus();

        for (Status status : statusList) {
            map.put(status.getId(), status.getName());
        }

        return map;
    }
}
