package Cyber_Community.api.controllers;
import Cyber_Community.entities.User;
import Cyber_Community.entities.ServiceBean.UserServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("/api")
@RestController
public class UserRestController {
    @Autowired
    UserServiceBean userService;

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/logged/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User user = userService.getUser(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/new")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        userService.add(user);
        return user;
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User newUser) {
        User oldUser = userService.getUser(id);
        if (oldUser != null) {
            userService.update(id,newUser);
            return new ResponseEntity<>(oldUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {

        User user =userService.getUser(id);
        userService.removeUser(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }



}
