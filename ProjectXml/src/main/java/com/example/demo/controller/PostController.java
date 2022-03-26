package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @RequestMapping(value = "/create-post", method = RequestMethod.GET)
    public String getCreatePostPage(@ModelAttribute("post") Post post) {
        return "USER/post-create";
    }

    @RequestMapping(value = "/edit-post", method = RequestMethod.GET)
    public String getEditPostPage(HttpServletRequest request, @ModelAttribute("post") Post post,
            @RequestParam String id, ModelMap model)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {
        for (Post p : postService.readListPost()) {
            if (p.getId().equalsIgnoreCase(id.trim())) {
                model.addAttribute("id", id);
                model.addAttribute("post", p);
                return "USER/post-edit";
            }
        }
        return "redirect:/selectPostListU";
    }

    @RequestMapping(value = "/registerPost", method = RequestMethod.POST)
    public String registerPost(HttpServletRequest request, @ModelAttribute("post") Post post) throws Exception {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate2 = LocalDate.now();
        Account account = (Account) request.getSession().getAttribute("account");

        post.setDatecreate(dtf2.format(localDate2));
        post.setDateupdate(dtf2.format(localDate2));
        post.setUserid(account.getId());
        post.setStatusid("1");
        post.setIsdelete("N");
        post.setIdisvisible("Y");
        List<String> likes = new ArrayList<>();
        likes.add(account.getId());
        post.setLikes(likes);
        try {
            postService.addNewPosts(post);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "redirect:/selectPostViewOfU";
    }

    @RequestMapping(value = "/updatePost", method = RequestMethod.POST)
    public String updatePost(HttpServletRequest request, @ModelAttribute("post") Post post, @RequestParam String id)
            throws Exception {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate2 = LocalDate.now();

        List<Post> posts = postService.readListPost();

        for (Post postItem : posts) {
            if (id.equals(postItem.getId())) {
                postItem.setDesription(post.getDesription());
                postItem.setTitle(post.getTitle());
                postItem.setImg(post.getImg());
                postItem.setDateupdate(dtf2.format(localDate2));
                postItem.setId(id);
                postService.updatePosts(postItem);
                break;
            }
        }

        return "redirect:/selectPostViewOfU";
    }

}
