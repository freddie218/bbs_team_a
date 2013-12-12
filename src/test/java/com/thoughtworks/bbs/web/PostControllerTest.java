package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostLikeService;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.PostBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import sun.security.acl.PrincipalImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: jw
 * Date: 13-11-22
 * Time: 下午5:00
 * To change this template use File | Settings | File Templates.
 */
public class PostControllerTest {
    private PostService postService;
    private UserService userService;
    private PostLikeService postLikeService;
    private PostController postController;
    private HttpServletRequest request;
    private User user;

    private Principal principal;
    private RedirectAttributesModelMap model;
    private ModelAndView expected;
    private ModelAndView result;
    PostBuilder postBuilder;

    @Before
    public void setup(){
        postService = mock(PostServiceImpl.class);
        userService = mock(UserServiceImpl.class);
        postLikeService = mock(PostLikeService.class);
        request = mock(HttpServletRequest.class);

        user = new User();
        user.setUserName("name");
        user.setPasswordHash("00000");
        user.setId(0L);

        when(userService.getByUsername("name")).thenReturn(user);
        when(request.getParameter("parentId")).thenReturn("0");

        model = new RedirectAttributesModelMap();
        principal = new PrincipalImpl("name");

        postController = new PostController(postService,userService);

    }

    @Test
    public void shouldJumpToHomeWhenCreatePostSuccess() throws IOException {

        when(request.getParameter("title")).thenReturn("hello");
        when(request.getParameter("content")).thenReturn("hello everyone");

        result = postController.processCreate(request,principal,model);
        expected = new ModelAndView("redirect:/");

        assertEquals("page should jump to home",expected.getViewName(),result.getViewName());
    }

    @Test
    public void shouldToPostCreateWhenTitleIsEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("");
        when(request.getParameter("content")).thenReturn("hello everyone");

        result = postController.processCreate(request,principal,model);
        expected= new ModelAndView("redirect:posts/create");

        assertEquals("page should jump to posts/create",expected.getViewName(),result.getViewName());

    }

    @Test
    public void shouldToPostCreateWhenContentIsEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("hello");
        when(request.getParameter("content")).thenReturn("");

        result = postController.processCreate(request,principal,model);
        expected= new ModelAndView("redirect:posts/create");

        assertEquals("page should jump to posts/create",expected.getViewName(),result.getViewName());

    }


    @Test
    public void shouldToPostCreateWhenTitleAndContentAreEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("");
        when(request.getParameter("content")).thenReturn("");

        result = postController.processCreate(request,principal,model);
        expected= new ModelAndView("redirect:posts/create");

        assertEquals("page should jump to posts/create",expected.getViewName(),result.getViewName());

    }

    @Test
    public void shouldToPostShowWhenReplyPostSuccess() throws IOException {

        when(userService.getByUsername("")).thenReturn(null);

        when(request.getParameter("title")).thenReturn("Re");
        when(request.getParameter("content")).thenReturn("OK");

        Long postId = 1L;
            result = postController.processReplyPost(postId, null,request,principal,model);
        expected = new ModelAndView("posts/show");

        assertEquals("page should stay posts/show",expected.getViewName(),result.getViewName());

    }


    @Test
    public void shouldToPostShowWhenReplyPostError() throws IOException {

        when(userService.getByUsername("")).thenReturn(null);

        when(request.getParameter("title")).thenReturn("Re");
        when(request.getParameter("content")).thenReturn("");
        Long postId = 1L;
        result = postController.processReplyPost(postId, null,request,principal,model);
        expected = new ModelAndView("posts/show");

        assertEquals("page should stay posts/show",expected.getViewName(),result.getViewName());

    }

    @Test
    public void shouldShowWarningWhenReplyPostError() {
        when(request.getParameter("title")).thenReturn("Re");
        when(request.getParameter("content")).thenReturn("");
        Long postId = 1L;
        postController.processReplyPost(postId,null,request,principal,model);
        assertTrue(model.containsAttribute("error"));
    }

    /*
    @Test
    public void shouldLikeThePost()
    {
        Post aPost = new Post().setPostId(10L).setLikeTime(0L);
        Long userID = userService.getByUsername(principal.getName()).getId();
        Long postID = 10L;
        PostLike aPostLike = new PostLike().setUserID(userID).setPostID(10L);
        when(request.getParameter("likePost")).thenReturn("10");
        result = postController.processLikePost(request, principal, model);
        expected = new ModelAndView("redirect:" + "10");



        assertEquals("page should stay posts/10:", expected.getViewName(), result.getViewName());
        verify(postService).save(new Post().setPostId(postID));
        verify(postLikeService).save(new PostLike().setPostID(postID).setUserID(userID));

    } */



}
