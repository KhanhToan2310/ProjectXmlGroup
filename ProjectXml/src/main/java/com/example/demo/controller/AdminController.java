package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /** accountService */
    @Autowired
    private AccountService accountService;

    /** roleService */
    @Autowired
    private RoleService roleService;

    /** postService */
    @Autowired
    private PostService postService;

    /** statusService */
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
            List<Post> resultList = postService.readListPost();

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
    @RequestMapping(value = "/updatePostAjax")
    public String updatePost(@RequestParam String id, @RequestParam String valCheckbox, @RequestParam String value)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        List<Post> resultList = postService.readListPost();
        Post temp = new Post();

        for (Post post : resultList) {
            if (id.equals(post.getId())) {
                temp = post;

                switch (value) {
                case "delete":
                    temp.setIsdelete(valCheckbox);
                    break;
                case "visible":
                    temp.setIdisvisible(valCheckbox);
                    break;
                case "select":
                    temp.setStatusid(valCheckbox);
                    break;
                default:
                    break;
                }

                postService.updatePosts(temp);

            }
        }

        return valCheckbox;
    }

    /**
     * select Post View
     * 
     * @param model
     * @return String
     * @throws XMLStreamException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws Exception
     */
    @RequestMapping(value = "/selectPostView")
    public String selectPostView(ModelMap model, @RequestParam String id)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        Post post = postService.findPost(id);
        List<Account> accountList = accountService.ReadListAccount();
        String userName = "";

        for (Account account : accountList) {
            if (post.getUserid().equals(account.getId())) {
                userName = account.getUsername();
                break;
            }
        }

        model.addAttribute("statusList", statusService.ReadListStatus());
        model.addAttribute("statusMap", selectStatusMap());
        model.addAttribute("userName", userName);
        model.addAttribute("post", post);
        return "ADMIN/examples/postView";
    }

    /**
     * select Account View
     * 
     * @param model
     * @return String
     * @throws XMLStreamException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws Exception
     */
    @RequestMapping(value = "/selectAccountView")
    public String selectAccountView(ModelMap model, @RequestParam String id)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        Account account = accountService.findAccount(id);
        Map<String, String> selectRoleMap = selectRoleMap();

        model.addAttribute("selectRoleMap", selectRoleMap);
        model.addAttribute("account", account);
        return "ADMIN/examples/accountView";
    }

    /**
     * select Account List
     * 
     * @param model
     * @return String
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
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

        return "ADMIN/examples/accountList";
    }

    /**
     * update Account
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
    @RequestMapping(value = "/updateAccountPostAjax")
    public String updateAccPost(@RequestParam String id, @RequestParam String valCheckbox, @RequestParam String value)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        List<Account> resultList = accountService.ReadListAccount();
        Account temp = new Account();

        for (Account account : resultList) {
            if (id.equals(account.getId())) {
                temp = account;

                if ("delete".equals(value)) {
                    temp.setIsdelete(valCheckbox);
                } else {
                    temp.setIsactive(valCheckbox);
                }
                if ("role".equals(value)) {
                    temp.setRole(valCheckbox);
                }

                accountService.UpdateUser(temp);

            }
        }

        return valCheckbox;
    }

    /**
     * select Find Account View
     * 
     * @param model
     * @param HttpServletRequest
     * @return String
     * @throws XMLStreamException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws Exception
     */
    @RequestMapping(value = "/updateAccountView")
    public String selectFindAccountView(ModelMap model , HttpServletRequest request)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

    	// get username on session
    	Account acc = (Account) request.getSession().getAttribute("account");

    			
    			// user id
    			String userPostedId = acc.getId();
    			
    	
        Account account = accountService.findAccount(userPostedId);
        Map<String, String> selectRoleMap = selectRoleMap();

        model.addAttribute("selectRoleMap", selectRoleMap);
        model.addAttribute("account", account);
        return "ADMIN/examples/accountForm";
    }

    /**
     * update Account Find
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
    @RequestMapping(value = "/updateAccountFind")
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
     * update Account Password
     * 
     * @param id
     * @param phone
     * @return null
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @ResponseBody
    @RequestMapping(value = "/updateAccountPassword")
    public String updateAccountPassword(@RequestParam String id, @RequestParam String newPassword)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        List<Account> resultList = accountService.ReadListAccount();

        for (Account account : resultList) {
            if (account.getId().equalsIgnoreCase(id)) {
                account.setPassword(newPassword);

                accountService.UpdateUser(account);
            }
        }

        return null;
    }
    
    /**
     * delete Form View
     * 
     * @param model
     * @param HttpServletRequest
     * @return String
     * @throws XMLStreamException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws Exception
     */
    @RequestMapping(value = "/deleteFormView")
    public String deleteFormView()
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

    	
    	
        return "ADMIN/examples/deleteForm";
    }
    
    /**
     * delete Action
     * 
     * @param model
     * @param HttpServletRequest
     * @return String
     * @throws XMLStreamException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/deleteAction")
    public String deleteAction()
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

    	accountService.DeleteUser();
    	postService.deletePosts();

        return "ok";
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

    /**
     * select Status Map
     * 
     * @return Map<String, String>
     */
    private Map<String, String> selectStatusMap() {
        Map<String, String> map = new HashMap<String, String>();
        List<Status> statusList = statusService.ReadListStatus();

        for (Status status : statusList) {
            map.put(status.getId(), status.getName());
        }

        return map;
    }
}
