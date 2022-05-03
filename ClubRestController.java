package Cyber_Community.api.controllers;

import Cyber_Community.entities.*;
import Cyber_Community.entities.ServiceBean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@RequestMapping("/api")
@RestController
public class ClubRestController {
    @Autowired
    ClubServiceBean clubService;
    @Autowired
    BlogServiceBean blogService;


    @GetMapping("/club")
    public ResponseEntity<Collection<Club>> getClub(){
        Collection<Club> clubs=clubService.findAll();
        if(clubs!=null) {
            return new ResponseEntity<>(clubs, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/club/{id}")
    public ResponseEntity<Club> getClub(@PathVariable long id){
        //return new ResponseEntity<>(this.clubService.findOne(id),HttpStatus.OK);
        Club club=new Club();
        if(club!=null) {
            return new ResponseEntity<>(this.clubService.findOne(id), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/club/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Club postClub(@RequestBody Club club){
        this.clubService.create(club);
        return club;
    }


    @DeleteMapping("/club/{id}")
    public ResponseEntity deleteClub(@PathVariable long id){
        Club club=this.clubService.findOne(id);
        this.clubService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/club/{id}")
    public ResponseEntity<Club> putClub(@PathVariable long id,@RequestBody Club club){
        Club aux=clubService.findOne(id);
        if(aux!=null) {
            this.clubService.update(club,id);
            return new ResponseEntity<>(this.clubService.findOne(id),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Club Search
    @GetMapping("/clubSearch/{value}")
    public List<Club> ClubSearchByName(@PathVariable String value){

        return clubService.findClubsByNameContaining(value);
    }




    //BlogRestController
    @GetMapping("/club/{idC}/blog/{idB}") //View one blog
    public ResponseEntity<Blog> getBlog(@PathVariable long idC, @PathVariable long idB) {
        Blog blog = blogService.findOne(idB);
        if (blog != null){
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/club/{id}/blog/new") //Add a new blog to the club
    @ResponseStatus(HttpStatus.CREATED)
    public Blog AddBlog(@PathVariable long id,@RequestBody Blog blog){
        blogService.create(id,blog);
        return blog;
    }

    @DeleteMapping("/club/{idC}/blog/{idB}") //Delete one blog
    public ResponseEntity<Blog> deleteBlog(@PathVariable long idC,@PathVariable long idB){
        Blog blog = this.blogService.findOne(idB);
        this.blogService.delete(idB);
        if(blog != null){
            return new ResponseEntity<>(blog, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/club/{idC}/blog/{idB}") //Update one blog
    public ResponseEntity<Blog> updateBlog(@PathVariable long idC,@PathVariable long idB,@RequestBody Blog upBlog){
        Blog blog = this.blogService.findOne(idB);
        if (blog != null){
            this.blogService.update(upBlog,idB);
            return new ResponseEntity<>(blog, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Search blog
    @GetMapping ("/club/blogSearch") //show all blogs
    public List<Blog> allBlogs(){
        return (List<Blog>) blogService.findAll();
    }

    @GetMapping("/club/blogSearch/{value}") //Title
    public List<Blog> blogSearchByTitle(@PathVariable String value){
        /*TypedQuery<Blog> query = entityManager.createQuery(
                "Select c from Blog c where c.title = :value", Blog.class
        );
        return query.setParameter("value", value).getSingleResult();
        */
        return blogService.byTitle(value);
    }

    /*
    @GetMapping("/club/blogSearch/{date}") //Date
    public List<Blog> blogSearchByDate(@PathVariable Date date){

        TypedQuery<Blog> query = entityManager.createQuery(

                "Select c from Blog c where c.date = :value", Blog.class
        );
        return query.setParameter("value", value).getSingleResult();

        return blogService.byDate(date);
    }
    */

}
