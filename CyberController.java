package Cyber_Community.web.controllers;

/*
 *  Handle the main page
 */


import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@Transactional
public class CyberController {

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("notlogged",true);
        model.addAttribute("logged",false);
        model.addAttribute("admin",false);
        return "Index/index";
    }

    @GetMapping("/lmau")
    public String lmau(){return "Logged/Lmau_template";}


    @GetMapping("/Login")
    public String login() {
        return "Login/Login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "Login/signUp";
    }


    @GetMapping("/upUser")
    public String upUser(){return "User/EditUser_template";}

}
