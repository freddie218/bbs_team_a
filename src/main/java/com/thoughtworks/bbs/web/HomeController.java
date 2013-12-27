package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.PostLike;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostLikeService;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostLikeServiceImpl;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.MyBatisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {
    PostService postService;
    UserService userService;
    PostLikeService postLikeService;

    public HomeController(){
        postService = new PostServiceImpl(MyBatisUtil.getSqlSessionFactory());
        userService = new UserServiceImpl(MyBatisUtil.getSqlSessionFactory());
        postLikeService = new PostLikeServiceImpl(MyBatisUtil.getSqlSessionFactory());
    }

    public HomeController(PostService service, UserService userService, PostLikeService postLikeService) {
        this.postService = service;
        this.userService = userService;
        this.postLikeService = postLikeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute("post") Post post, Principal principal) {
        if (null == principal) {
            return "login";
        }
        displayHomePage(principal, model);
        return "home";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView likeProcess(HttpServletRequest request, Principal principal, Model model)
    {
        String likedPostID = request.getParameter("likePost");
        Long userID = userService.getByUsername(principal.getName()).getId();
        PostLike aPostLike = new PostLike().setPostID(Long.parseLong(likedPostID)).setUserID(userID);
        postLikeService.save(aPostLike);

        Long like_time = postService.get(Long.parseLong(likedPostID)).getLiked_time();
        Post newPost = postService.get(Long.parseLong(likedPostID)).setLikeTime(like_time + 1);
        postService.save(newPost);

        displayHomePage(principal, model);
        return new ModelAndView("home");
    }

    private void displayHomePage(Principal principal, Model model) {
        List<Post> posts = postService.findAllPostsOrderByTime();
        List<User> users = new ArrayList<User>();
        List<Boolean> ifLiked = new ArrayList<Boolean>();
        Long userId = userService.getByUsername(principal.getName()).getId();

        for(Post eachPost : posts) {
            String name = eachPost.getAuthorName();
            User user = userService.getByUsername(name);
            users.add(user);
            ifLiked.add(postLikeService.isLiked(userId, eachPost.getPostId()));
        }
        model.addAttribute("posts",posts);
        model.addAttribute("users",users);
        model.addAttribute("ifLike", ifLiked);
    }

    @RequestMapping(value = {"/search"},method = RequestMethod.GET)
    public ModelAndView searchPost(HttpServletRequest request, Principal principal, ModelMap map) {
        String author = request.getParameter("author");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        List<Post> posts = postService.searchPost(author, title, content, start, end);
        List<User> users = new ArrayList<User>();
        List<Boolean> ifLiked = new ArrayList<Boolean>();
        Long userId = userService.getByUsername(principal.getName()).getId();

        for(Post eachPost : posts) {
            String name = eachPost.getAuthorName();
            User user = userService.getByUsername(name);
            users.add(user);
            ifLiked.add(postLikeService.isLiked(userId, eachPost.getPostId()));
        }
        map.addAttribute("posts",posts);
        map.addAttribute("users",users);
        map.addAttribute("ifLike", ifLiked);
        map.addAttribute("number", posts.size());
        return new ModelAndView("home",map);
    }

    @RequestMapping(value = {"/search"},method = RequestMethod.POST)
    public ModelAndView searchLike(HttpServletRequest request, Principal principal, ModelMap model)
    {
        String likedPostID = request.getParameter("likePost");
        Long userID = userService.getByUsername(principal.getName()).getId();
        PostLike aPostLike = new PostLike().setPostID(Long.parseLong(likedPostID)).setUserID(userID);
        postLikeService.save(aPostLike);
        Long like_time = postService.get(Long.parseLong(likedPostID)).getLiked_time();
        Post newPost = postService.get(Long.parseLong(likedPostID)).setLikeTime(like_time + 1);
        postService.save(newPost);
        return searchPost(request,principal,model);
    }

    @RequestMapping(value = {"/top"}, method = RequestMethod.POST)
    public ModelAndView setTopMost(HttpServletRequest request, Principal principal, Model model)
    {
        String postID = request.getParameter("postIdToTop");
        postService.setTopMostPost(postID);
        return new ModelAndView("redirect:/");
    }
}
