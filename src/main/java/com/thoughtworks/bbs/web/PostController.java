package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.MyBatisUtil;
import com.thoughtworks.bbs.util.PostBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/posts")
public class PostController {

    private PostService postService;
    private UserService userService;

    public PostController(){
        postService = new PostServiceImpl(MyBatisUtil.getSqlSessionFactory());
        userService = new UserServiceImpl(MyBatisUtil.getSqlSessionFactory());
    }

    public PostController(PostService postService,UserService userService){
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(value = {"/{postId}"}, method = RequestMethod.GET)
    public String get(@PathVariable("postId") Long postId, Model model, @ModelAttribute Post post, Principal principal) {
        model.addAttribute("mainPost", postService.get(postId));
        model.addAttribute("posts", postService.findAllPostByMainPost(postId));
        return "posts/show";
    }

    @RequestMapping(value = {"/{postId}"}, method = RequestMethod.POST)
    public ModelAndView processReplyPost(@PathVariable("postId") Long postId, @ModelAttribute Post post, HttpServletRequest request, Principal principal,Model model) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        User currentUser = userService.getByUsername(principal.getName());

        if(isContentEmpty(content)){
            model.addAttribute("error","true");
            model.addAttribute("mainPost", postService.get(postId));
            model.addAttribute("posts", postService.findAllPostByMainPost(postId));
            return new ModelAndView("posts/show");
        }
        PostBuilder builder = new PostBuilder();
        builder.title(title).content(content).author(currentUser.getUserName()).parentId(postId).creatorId(currentUser.getId())
                .modifierId(currentUser.getId()).createTime(new Date()).modifyTime(new Date());

        postService.save(builder.build());

        model.addAttribute("mainPost", postService.get(postId));
        model.addAttribute("posts", postService.findAllPostByMainPost(postId));
        return new ModelAndView("posts/show");
    }
    public boolean isContentEmpty(String content){
        return content.isEmpty();
    }


    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public ModelAndView createPostForm(ModelMap model, Principal principal) {
        return new ModelAndView("posts/create");
    }


    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ModelAndView processCreate(HttpServletRequest request, Principal principal,Model model) throws IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String parentId = request.getParameter("parentId");

        Long parentIdLong = 0L;

        if (!StringUtils.isEmpty(parentId)) {
            parentIdLong = Long.parseLong(parentId);
        }
        User currentUser = userService.getByUsername(principal.getName());

        if(isTitleOrContentEmpty(title,content)){
           model.addAttribute("error","true");
           return new ModelAndView("posts/create");
        }


        PostBuilder builder = new PostBuilder();
        builder.title(title).content(content).author(currentUser.getUserName()).parentId(parentIdLong).creatorId(currentUser.getId())
                .modifierId(currentUser.getId()).createTime(new Date()).modifyTime(new Date());

        postService.save(builder.build());

        model.addAttribute("posts", postService.findAllPostsOrderByTime());
        return new ModelAndView("home");
    }

    public boolean isTitleOrContentEmpty(String title, String content){
        return title.isEmpty() || content.isEmpty();
    }

}
