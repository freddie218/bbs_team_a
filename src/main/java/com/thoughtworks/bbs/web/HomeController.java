package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.util.MyBatisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {
    PostService postService;

    public HomeController(){
        postService = new PostServiceImpl(MyBatisUtil.getSqlSessionFactory());
    }
    public HomeController(PostService service) {
        postService = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute("post") Post post, Principal principal) {
        if (null == principal) {
            return "login";
        }

        model.addAttribute("posts", postService.findAllPostsOrderByTime());
        return "home";
    }
}
