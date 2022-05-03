package Cyber_Community.web.controllers;

import Cyber_Community.api.exceptions.NotFoundException;
import Cyber_Community.entities.User;
import Cyber_Community.entities.Repositories.UserRepository;
import Cyber_Community.entities.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    //For login:
    @GetMapping("/check")
    public String enterPage(User user,Model model){

        if ((userService.getID(user.getNickname()))!=-1) {

           // if () {
                model.addAttribute("User", true);
                model.addAttribute("notUser", false);
                model.addAttribute("U_id", userService.getID(user.getNickname()));
                model.addAttribute("message", "This user has been found");
           /* } else {
                model.addAttribute("User", false);
                model.addAttribute("notUser", true);
                model.addAttribute("message", "Invalid password");
            }*/
        } else {
            model.addAttribute("User",false);
            model.addAttribute("notUser",true);
            model.addAttribute("U_id", -1);
           model.addAttribute("message", "This user doesn't exist or the password is incorrect");
        }
        return "message2";
    }


    //Create new user
    @PostMapping("/new")
    public String createUser(User newUser,Model model) {
            this.userService.add(newUser);
            model.addAttribute("User", true);
            model.addAttribute("U_id", newUser.getId_User());
            model.addAttribute("message","This user has been created");
            return "message2";

    }

    @GetMapping("/update/{id}") //edit a user-get id
    public String updUser(Model model, User user, @PathVariable long id) {
        this.userService.getUser(id);
        model.addAttribute("U_id", user.getId_User());
        model.addAttribute("notAdmin",true);
        model.addAttribute("admin",false);
        return "User/EditUser_template";
    }

    //Modify an existing user
    @PostMapping("/updated/{id}/user")
    public String updateUser(Model model, User user, @PathVariable long id) {
        this.userService.add(user);
        this.userService.update(id, this.userService.getUser(id));
        model.addAttribute("U_id", user.getId_User());
        model.addAttribute("message", "The user has been edited");
        return "message2";
    }

    //Delete an existing user
    @GetMapping("/delete/{id}")
    public String deleteUser(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getUser(id));
        userService.removeUser(id);
        model.addAttribute("message", "The user has been deleted");
        return "message";
    }

    //Auxiliar functions
    @GetMapping("/search/byName")
    public String searchByName(Model model){
        model.addAttribute("isClub",false);
        model.addAttribute("isUser",true);
        model.addAttribute("users",userService.getUsers());
        return "Index/search";
    }

    @GetMapping("/seeProfile/{id}")
    public String seeProfile(Model model, @PathVariable long id) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new NotFoundException("User " + id + " not found");
        } else {
            model.addAttribute("user", userService.getUser(id));
            model.addAttribute("id_User", id);
            return "User/User_seeProfile";
        }
    }
}
