package Cyber_Community.web.controllers;

import Cyber_Community.entities.*;
import Cyber_Community.entities.Service.UserService;
import Cyber_Community.entities.ServiceBean.ClubServiceBean;
import Cyber_Community.web.error_handing.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


@Controller
@RequestMapping("/logged/{id}")
public class LoggedController {
    @Autowired
    UserService userService;
    @Autowired
    ClubServiceBean clubService;
    @Autowired
    private EntityManager entityManager;
    
    //Initial page once you log in
    @GetMapping("")
    public String loggedUser (Model model,@PathVariable long id){
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("notAdmin",true);
        model.addAttribute("admin",false);
        return "Logged/loggedIndex";
    }

    //Once the person is logged in the website
    @GetMapping("/lmau")
    public String lmauLog(Model model,@PathVariable long id){
        model.addAttribute("user", userService.getUser(id));
        return "Logged/LmauLog_template";
    }
    @GetMapping("/admin/lmau")
    public String lmauLogAd(Model model,@PathVariable long id){
        model.addAttribute("user", userService.getUser(id));
        return "Logged/LmauLogAd_template";
    }

    @GetMapping("/club")
    public String logClubPage (Model model, @PathVariable long id){
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("logged",true);
        model.addAttribute("not logged",false);
        model.addAttribute("notAdmin",true);
        model.addAttribute("admin",false);
        model.addAttribute("clubs", clubService.findAll());
        return "Club/IndexClub_template";
    }

    @GetMapping("/admin")
    public String loggedAdmin(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("notAdmin",false);
        model.addAttribute("admin",true);
        return "Logged/loggedIndex";
    }

   @GetMapping("/admin/club")
    public String logAdminClub(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("logged",true);
        model.addAttribute("not logged",false);
        model.addAttribute("notAdmin",false);
        model.addAttribute("admin",true);
        model.addAttribute("clubs", clubService.findAll());
        return "Club/IndexClub_template";
    }

    //Get user info, for now we have to write the id, it will be able to make it on its own when we start working with databases.
    @GetMapping("/user")
    public String getById(Model model, @PathVariable long id) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new NotFoundException("User " + id + " not found");
        } else {
            model.addAttribute("notAdmin",true);
            model.addAttribute("admin",false);
            model.addAttribute("user", userService.getUser(id));
            model.addAttribute("id_User", id);
            return "User/User_template";
        }
    }
    @GetMapping("/admin/user")
    public String AdGetById(Model model, @PathVariable long id) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new NotFoundException("User " + id + " not found");
        } else {
            model.addAttribute("user", userService.getUser(id));
            model.addAttribute("notAdmin",false);
            model.addAttribute("admin",true);
            model.addAttribute("id_User", id);
            return "/User/User_template";
        }
    }

    @GetMapping("/admin/user/users")
    public String usersList(Model model, @PathVariable long id) {
        model.addAttribute("all", true);
        model.addAttribute("notAll", false);
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("message", "All users:");
        return "/User/UserList_template";
    }

    @GetMapping("/admin/user/usersQ")
    public String usersQ(Model model, String nickname, @PathVariable long id) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("user", userService.getUser(id));
            if ((nickname!=null) && (userService.equalNickName(nickname))){
                model.addAttribute("message", "User found");
                model.addAttribute("id_User", id);
                model.addAttribute("all", false);
                model.addAttribute("notAll", true);
                model.addAttribute("serUser", userService.getUser(userService.getID(nickname)));
            } else if ((nickname!=null) && !(userService.equalNickName(nickname))){
                model.addAttribute("message", "The user doesn't exist.");
                model.addAttribute("all", true);
                model.addAttribute("notAll", false);
            } /*else if (nickname==null){
                model.addAttribute("message", "All users:");
                model.addAttribute("all", true);
                model.addAttribute("notAll", false);
            }*/
        return "/User/UserList_template";
    }
}
