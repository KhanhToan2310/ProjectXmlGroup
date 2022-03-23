package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Account;
import com.example.demo.model.Role;
import com.example.demo.service.AccountService;
import com.example.demo.service.RoleService;

@Controller
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/selectPostList")
    public String selectPostList() {
        return "ADMIN/examples/postList";
    }

    // selectAccountView
    @RequestMapping("/selectAccountList")
    public String selectAccountList(Model model)
            throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException {

        Map<String, String> selectRoleMap = selectRoleMap();
        List<Account> accountList = accountService.ReadListAccount();
        List<Account> list = new ArrayList<Account>();

        for (Account account : accountList) {
            if (!"1".equals(account.getRole())) {
                list.add(account);
            }
        }

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
}
