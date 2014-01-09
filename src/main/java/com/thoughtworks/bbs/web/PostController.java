package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.PostLike;
import com.thoughtworks.bbs.model.PostTag;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostLikeService;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.PostTagService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostLikeServiceImpl;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.PostTagServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.MyBatisUtil;
import com.thoughtworks.bbs.util.PostBuilder;
import com.thoughtworks.bbs.util.ViolationChecker;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/posts")
public class PostController {

    private PostService postService = new PostServiceImpl(MyBatisUtil.getSqlSessionFactory());
    private UserService userService = new UserServiceImpl(MyBatisUtil.getSqlSessionFactory());
    private PostLikeService postLikeService = new PostLikeServiceImpl(MyBatisUtil.getSqlSessionFactory());
    private PostTagService postTagService = new PostTagServiceImpl(MyBatisUtil.getSqlSessionFactory());

    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPostLikeService(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    public void setPostTagService(PostTagService postTagService) {
        this.postTagService = postTagService;
    }

    @RequestMapping(value = {"/{postId}"}, method = RequestMethod.GET)
    public String get(@PathVariable("postId") Long postId, Model model, @ModelAttribute Post post, Principal principal) {
        model.addAttribute("mainPost", postService.get(postId));
        model.addAttribute("posts", postService.findAllPostByMainPost(postId));

        if(postService.get(postId).getAuthorName().equals(principal.getName())) {
            model.addAttribute("isMyMainPost", "True");
        }

        Long userID = userService.getByUsername(principal.getName()).getId();
        model.addAttribute("like", postLikeService.isLiked(userID, postId));
        model.addAttribute("tagLabels", getTags(postTagService.getPostTagByPostID(postId)));
        return "posts/show";
    }

    private List<String> getTags(List<PostTag> allTags) {
        List<String> tags = new ArrayList<String>();
        for(PostTag postTag : allTags)
        {
            tags.add(postTag.getATag());
        }
        return tags;
    }

    @RequestMapping(value = {"/{postId}"}, method = RequestMethod.POST)
    public ModelAndView processReplyPost(@PathVariable("postId") Long postId, @ModelAttribute Post post,
                                         HttpServletRequest request, Principal principal,RedirectAttributesModelMap model) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        User currentUser = userService.getByUsername(principal.getName());

        if(isContentEmpty(content)){
            model.addFlashAttribute("error","true");
        }
        else if(!ViolationChecker.getInstance().contentLegal(content + " " + title)) {
            model.addFlashAttribute("illegal", "根据国家相关法律和政策，您的帖子不能发布。");
        }
        else {
            PostBuilder builder = new PostBuilder();
            builder.title(title).content(content).author(currentUser.getUserName()).parentId(postId).creatorId(currentUser.getId())
                    .modifierId(currentUser.getId()).createTime(new Date()).modifyTime(builder.build().getCreateTime()).likedTime(0L);

            postService.save(builder.build());
        }

        return new ModelAndView("redirect:" + postId);
    }

    @RequestMapping(value = {"/del/{postId}"}, method = RequestMethod.POST)
    public ModelAndView processPostDeletion(HttpServletRequest request, @PathVariable("postId") Long postId,
                                            RedirectAttributesModelMap model, @ModelAttribute Post post, Principal principal) {
        Post postToDelete = postService.get(Long.parseLong(request.getParameter("postIdToDel")));
        postService.delete(postToDelete);
        if(postToDelete.getParentId().equals(0L)) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/posts/" + postId);
    }

    @RequestMapping(value = {"/likeProcess"}, method = RequestMethod.POST)
    public ModelAndView processLikePost(HttpServletRequest request, Principal principal, RedirectAttributesModelMap model)
    {
        String likedPostID = request.getParameter("likePost");
        PostLike aPostLike = new PostLike().setPostID(Long.parseLong(likedPostID)).setUserID(userService.getByUsername(principal.getName()).getId());
        postLikeService.save(aPostLike);

        Long like_time = postService.get(Long.parseLong(likedPostID)).getLiked_time();
        postService.save(postService.get(Long.parseLong(likedPostID)).setLikeTime(like_time + 01L));

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
    public ModelAndView processCreate(HttpServletRequest request, Principal principal, ModelMap model) throws IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String parentId = request.getParameter("parentId");
        String tag = request.getParameter("allTags");

        Long parentIdLong = 0L;

        if (!StringUtils.isEmpty(parentId)) {
            parentIdLong = Long.parseLong(parentId);
        }
        User currentUser = userService.getByUsername(principal.getName());

        if(isTitleOrContentEmpty(title,content)){
           model.addAttribute("error","true");
           return new ModelAndView("posts/create");
        }
        else {
            if(!ViolationChecker.getInstance().contentLegal(content + " " + title + " " + tag)) {
                model.addAttribute("illegal", "根据国家相关法律和政策，您的帖子不能发布。");
                return new ModelAndView("posts/create");
            }
        }

        PostBuilder builder = new PostBuilder();
        builder.title(title).content(content).author(currentUser.getUserName()).parentId(parentIdLong).creatorId(currentUser.getId())
                .modifierId(currentUser.getId()).createTime(new Date()).modifyTime(builder.build().getCreateTime()).likedTime(0L);

        Post aPost = builder.build();
        postService.save(aPost);

        String[] tagSplit = tag.split("[,;]");
        for(String s : tagSplit)
        {
            postTagService.save(new PostTag().setATag(s).setPostID(
                    postService.getPostIdByAuthorAndCreateTime(aPost.getAuthorName(), aPost.getCreateTime())));
        }

        return new ModelAndView("redirect:/");
    }

    private boolean isTitleOrContentEmpty(String title, String content){
        return title.trim().isEmpty() || content.trim().isEmpty();
    }
}
