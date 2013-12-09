package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.MyBatisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    PostService postService;
    UserService userService;

    public HomeController(){
        postService = new PostServiceImpl(MyBatisUtil.getSqlSessionFactory());
        userService = new UserServiceImpl(MyBatisUtil.getSqlSessionFactory());
    }
    public HomeController(PostService service, UserService userService ) {
        postService = service;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute("post") Post post, Principal principal) {
        if (null == principal) {
            return "login";
        }

        List<Post> posts = postService.findAllPostsOrderByTime();
        List<User> users = new ArrayList<User>();
        for(Post eachPost : posts) {
            String name = eachPost.getAuthorName();
            User user = userService.getByUsername(name);
            users.add(user);
        }
        model.addAttribute("posts",posts);
        model.addAttribute("users",users);


        return "home";
    }
}
