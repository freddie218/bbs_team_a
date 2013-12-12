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

        List<Post> posts = postService.findAllPostsOrderByTime();
        List<User> users = new ArrayList<User>();
        for(Post eachPost : posts) {
            String name = eachPost.getAuthorName();
            User user = userService.getByUsername(name);
            users.add(user);
        }
        model.addAttribute("posts",posts);
        model.addAttribute("users",users);

        Map<Post, Boolean> postsWithLike = new HashMap<Post, Boolean>();
        for(Post aPost : posts)
        {
            Long userID = userService.getByUsername(principal.getName()).getId();
            postsWithLike.put(aPost, postLikeService.isLiked(userID, aPost.getPostId()));
        }

        model.addAttribute("postsWithLike", postsWithLike);

        return "home";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView likeProcess(HttpServletRequest request, Principal principal, Model model)
    {
        String likedPostID = request.getParameter("likePost");
        Long userID = userService.getByUsername(principal.getName()).getId();
        PostLike aPostLike = new PostLike().setPostID(Long.parseLong(likedPostID)).setUserID(userID);

        postLikeService.save(aPostLike);

        List<Post> posts = postService.findAllPostsOrderByTime();
        List<User> users = new ArrayList<User>();
        for(Post eachPost : posts) {
            String name = eachPost.getAuthorName();
            User user = userService.getByUsername(name);
            users.add(user);
        }
        model.addAttribute("posts",posts);
        model.addAttribute("users",users);

        Map<Post, Boolean> postsWithLike = new HashMap<Post, Boolean>();
        for(Post aPost : posts)
        {
            Long user_ID = userService.getByUsername(principal.getName()).getId();
            postsWithLike.put(aPost, postLikeService.isLiked(user_ID, aPost.getPostId()));
        }

        model.addAttribute("postsWithLike", postsWithLike);

        return new ModelAndView("home");
    }
}
