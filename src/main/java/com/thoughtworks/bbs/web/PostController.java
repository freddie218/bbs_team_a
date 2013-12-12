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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private PostService postService;
    private UserService userService;
    private PostLikeService postLikeService;

    public PostController(){
        postService = new PostServiceImpl(MyBatisUtil.getSqlSessionFactory());
        userService = new UserServiceImpl(MyBatisUtil.getSqlSessionFactory());
        postLikeService = new PostLikeServiceImpl(MyBatisUtil.getSqlSessionFactory());
    }

    public PostController(PostService postService,UserService userService){
        this.postService = postService;
        this.userService = userService;
    }

    public PostController (PostService postService,UserService userService, PostLikeService postLikeService)
    {
        this.postService = postService;
        this.userService = userService;
        this.postLikeService = postLikeService;
    }

    @RequestMapping(value = {"/{postId}"}, method = RequestMethod.GET)
    public String get(@PathVariable("postId") Long postId, Model model, @ModelAttribute Post post, Principal principal) {
        model.addAttribute("mainPost", postService.get(postId));
        model.addAttribute("posts", postService.findAllPostByMainPost(postId));

        Long userID = userService.getByUsername(principal.getName()).getId();
        model.addAttribute("like", postLikeService.isLiked(userID, postId));
        return "posts/show";
    }

    @RequestMapping(value = {"/{postId}"}, method = RequestMethod.POST)
    public ModelAndView processReplyPost(@PathVariable("postId") Long postId, @ModelAttribute Post post, HttpServletRequest request, Principal principal,Model model) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        User currentUser = userService.getByUsername(principal.getName());

        if(isContentEmpty(content)){
            model.addAttribute("error","true");
        }
        PostBuilder builder = new PostBuilder();
        builder.title(title).content(content).author(currentUser.getUserName()).parentId(postId).creatorId(currentUser.getId())
                .modifierId(currentUser.getId()).createTime(new Date()).modifyTime(builder.build().getCreateTime()).likedTime(0L);

        postService.save(builder.build());

        model.addAttribute("mainPost", postService.get(postId));
        model.addAttribute("posts", postService.findAllPostByMainPost(postId));

        return new ModelAndView("posts/show");
    }

    @RequestMapping(value = {"/likeProcess"}, method = RequestMethod.POST)
    public ModelAndView processLikePost(HttpServletRequest request, Principal principal, RedirectAttributesModelMap model)
    {
        User currentUser = userService.getByUsername(principal.getName());
        String likedPostID = request.getParameter("likePost");
        Long userID = currentUser.getId();
        PostLike aPostLike = new PostLike().setPostID(Long.parseLong(likedPostID)).setUserID(userID);

        Long like_time = postService.get(Long.parseLong(likedPostID)).getLiked_time();
        like_time++;
        Post newPost = postService.get(Long.parseLong(likedPostID)).setLikeTime(like_time);
        postService.save(newPost);

        postLikeService.save(aPostLike);

        model.addFlashAttribute("mainPost", postService.get(Long.parseLong(likedPostID)));
        model.addFlashAttribute("posts", postService.findAllPostByMainPost(Long.parseLong(likedPostID)));

        return new ModelAndView("redirect:" + likedPostID);
    }

    private boolean isContentEmpty(String content){
        return content.isEmpty();
    }


    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public ModelAndView createPostForm(ModelMap model, Principal principal) {
        return new ModelAndView("posts/create");
    }


    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ModelAndView processCreate(HttpServletRequest request, Principal principal,RedirectAttributesModelMap model) throws IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String parentId = request.getParameter("parentId");

        Long parentIdLong = 0L;

        if (!StringUtils.isEmpty(parentId)) {
            parentIdLong = Long.parseLong(parentId);
        }
        User currentUser = userService.getByUsername(principal.getName());

        if(isTitleOrContentEmpty(title,content)){
           model.addFlashAttribute("error","true");
           return new ModelAndView("redirect:posts/create");
        }

        PostBuilder builder = new PostBuilder();
        builder.title(title).content(content).author(currentUser.getUserName()).parentId(parentIdLong).creatorId(currentUser.getId())
                .modifierId(currentUser.getId()).createTime(new Date()).modifyTime(builder.build().getCreateTime()).likedTime(0L);

        postService.save(builder.build());

        model.addFlashAttribute("posts", postService.findAllPostsOrderByTime());

        List<Post> posts = postService.findAllPostsOrderByTime();
        List<User> users = new ArrayList<User>();
        for(Post eachPost : posts) {
            String name = eachPost.getAuthorName();
            User user = userService.getByUsername(name);
            users.add(user);
        }
        model.addFlashAttribute("posts",posts);
        model.addFlashAttribute("users",users);

        Map<Post, Boolean> postsWithLike = new HashMap<Post, Boolean>();
        for(Post aPost : posts)
        {
            Long user_ID = userService.getByUsername(principal.getName()).getId();
            postsWithLike.put(aPost, postLikeService.isLiked(user_ID, aPost.getPostId()));
        }

        model.addFlashAttribute("postsWithLike", postsWithLike);
        return new ModelAndView("redirect:/");
    }

    private boolean isTitleOrContentEmpty(String title, String content){
        return title.isEmpty() || content.isEmpty();
    }

}
