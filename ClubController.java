package Cyber_Community.web.controllers;


import Cyber_Community.entities.*;
import Cyber_Community.entities.ServiceBean.BlogServiceBean;
import Cyber_Community.entities.ServiceBean.ClubServiceBean;
import Cyber_Community.entities.ServiceBean.UserServiceBean;
import Cyber_Community.web.error_handing.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RequestMapping("/club")
@Controller
/*@Transactional
//@PreAuthorize("hasAnyRole('ADMIN')")
@PreAuthorize("hasAuthority('club:write')")
//hasPermission*/

public class ClubController {
    @Autowired
    ClubServiceBean clubService;
    @Autowired
    BlogServiceBean blogService;
    @Autowired
    UserServiceBean userService;

    @Autowired
    private EntityManager entityManager;

    private Long idC;
    private Long idB;

    @GetMapping("") //View all clubs
    public String club(Model model) {
        model.addAttribute("notlogged",true);
        model.addAttribute("logged", false);
        model.addAttribute("admin", false);
        model.addAttribute("clubs", clubService.findAll());
        return "Club/IndexClub_template";
    }


    @GetMapping("/{id}")  //View one club
    public String findOne(Model model, @PathVariable long id) {
        Club club = clubService.findOne(id);
        if (club == null) {
            throw new NotFoundException("Club " + id + " not found");
        } else {
            model.addAttribute("club", clubService.findOne(id));
            model.addAttribute("blog", clubService.findOne(id).getBlogs());

        }
        return "Club/Club_template";
    }

    @PostMapping("/new") //Create a club
    @ResponseStatus(HttpStatus.CREATED)
    public String postClub(Model model, Club club) {
        club.setDefaultSetting();
        clubService.create(club);
        model.addAttribute("message", "This club has been created");
        return "message";
    }


    @GetMapping("/update") //Create a club

    public String postClub1(Model model, Club club) {
        return "Club/AddClub";
    }

    @GetMapping("/delete") //delete a club
    public String deleteClub(Model model) {
        model.addAttribute("clubs", clubService.findAll());
        return "Club/ClubDelete_template";
    }

    @GetMapping(("/delete/{id}")) //delete a club-get id
    public String deleteClub(Model model, @PathVariable long id) {
        Club club = clubService.findOne(id);
        if (club == null) {
            throw new NotFoundException("Club " + id + " not found");
        }
        model.addAttribute("message", "This club has been deleted");
        clubService.delete(id);
        return "message";
    }

    @GetMapping("/Edit") //edit a club
    public String EditClub(Model model) {
        model.addAttribute("clubs", clubService.findAll());
        return "Club/ClubEdit_template";
    }

    @GetMapping("/edit/{id}") //Edit a club - get id
    public String putClub(@PathVariable long id) {
        this.idC = id;
        return "Club/EditClub";
    }

    @PostMapping("/edit") //Update club
    @ResponseStatus(HttpStatus.CREATED)
    public String EdiClub(Model model, Club club) {
        clubService.update(club,this.idC);
        model.addAttribute("logged",true);
        model.addAttribute("message", "This club has been edited");
        return "message";
    }

    @GetMapping("/logged/club") //Once the user is logged the page changes a bit
    public String logClubPage(Model model) {
        model.addAttribute("silent", true);
        model.addAttribute("clubs", clubService.findAll());
        return "Logged/LoggedIndexClub_template";
    }

    //Blog Contoller part
   @GetMapping("/{idC}/blog/{idB}") //View one blog
   public String viewBlog(Model model, @PathVariable long idC, @PathVariable long idB) {
       //If you are the author you can edit or delete the blog
       model.addAttribute("idC", idC);
       model.addAttribute("blog", blogService.findOne(idB));
       model.addAttribute("club", clubService.findOne(idC));
       model.addAttribute("silent", true);
       return "Blog/Blog_template";
   }

    @GetMapping("{id}/blog/new") //add a blog - get id of Club
    public String addBlog(Model model, @PathVariable long id) {
        this.idC = id;
        return "Blog/AddBlog";
    }

    @PostMapping( "/blog/new") //Add a new blog to the club
    @ResponseStatus(HttpStatus.CREATED)
    public String AddBlog(Model model, Blog blog) {
        this.blogService.create(this.idC,blog);
        model.addAttribute("message","This blog has been created");
        model.addAttribute("idC", this.idC);
        return "message";
    }

    @GetMapping("/{idC}/blog/{idB}/delete") //Delete one blog
    public String DeleteBlog(Model model, @PathVariable long idC, @PathVariable long idB){
        this.blogService.delete(idB);
        model.addAttribute("message","This blog has been deleted");
        return "message";
    }

    @GetMapping("/{idC}/blog/{idB}/edit") //Edit one blog - get Ids
    public String EditBlog(@PathVariable long idC, @PathVariable long idB){
        this.idC=idC;
        this.idB=idB;
        return "Blog/EditBlog";
    }

    @PostMapping("/blog/edit") //Update one blog
    public String UpdateBlog(Model model,Blog upBlog){
        blogService.update(upBlog,this.idB);
        model.addAttribute("message","This blog has been edited");
        return "message";
    }

    //Auxiliar function
    @GetMapping("/application")
    public String application(){
        return "PetitionClub";
    }



    @GetMapping("/{idC}/blog/{idB}/addLike")
    public String addLike(@PathVariable long idC, @PathVariable long idB){
        this.blogService.findOne(idB).addLike();
        return "redirect:/club/{idC}/blog/{idB}";
    }

    @GetMapping("search/byName")
    public String searchByName(Model model){
        //model.addAttribute("clubs",clubService.findByName(name));
        //index with clubs
        model.addAttribute("isClub",true);
        model.addAttribute("isUser",false);
        model.addAttribute("clubs",clubService.findAll());
        return "Index/search";
    }

    //Search blogs
    @GetMapping("/blogSearch")
    public String searchBlogs(Model model){
        model.addAttribute("blog",this.blogService.findAll());
        model.addAttribute("club",this.clubService.findAll());
        return "Blog/BlogSearch_template";
    }


    @PostMapping("/blogSearch") //Title
    public String blogSearchByTitle(@RequestParam String title, Model model){
        model.addAttribute("blog",this.blogService.byTitle(title));
        model.addAttribute("club",this.clubService.findAll());
        return "Blog/BlogSearch_template";
    }

/*
    @GetMapping("/blogSearch/{date}") //Date
    public List<Blog> blogSearchByDate(@PathVariable Date date){
        return
    }


    @Transactional
    @DeleteMapping("/blogSearch/{value}")
    public int borrar_blog (@PathVariable String value){
        Query query = entityManager.createQuery("delete from Blog c where c.title = :value");
        return query.setParameter("value",value).executeUpdate();
    }

    @Transactional
    @PutMapping("/blogSearch/{value}")
    public int put_blog (@PathVariable String value, @RequestBody Blog blog){
        String upValue = blog.getTitle();
        Query query = entityManager.createQuery("update Blog c set c.title =:upValue where c.title = :value");
        query.setParameter("value", upValue);
        return query.setParameter("value",value).executeUpdate();
    }

 */
}