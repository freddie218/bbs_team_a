package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.PostBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import sun.security.acl.PrincipalImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

import static org.junit.Assert.assertEquals;
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
    private PostService postservice;
    private UserService userService;
    private PostController postController;
    private HttpServletRequest request;
    private User user;

    private Principal principal;
    private Model model;
    private ModelAndView expected;
    private ModelAndView result;
    PostBuilder postBuilder;

    @Before
    public void setup(){
        postservice = mock(PostServiceImpl.class);
        userService = mock(UserServiceImpl.class);
        request = mock(HttpServletRequest.class);

        user = new User();
        user.setUserName("name");
        user.setPasswordHash("00000");
        user.setId(0L);

        when(userService.getByUsername("name")).thenReturn(user);
        when(request.getParameter("parentId")).thenReturn("0");

        model = new ExtendedModelMap();
        principal = new PrincipalImpl("name");

        postController = new PostController(postservice,userService);

    }

    @Test
    public void shouldJumpToHomeWhenCreatePostSuccess() throws IOException {

        when(request.getParameter("title")).thenReturn("hello");
        when(request.getParameter("content")).thenReturn("hello everyone");

        result = postController.processCreate(request,principal,model);
        expected = new ModelAndView("home");

        assertEquals("page should jump to home",expected.getViewName(),result.getViewName());
    }

    @Test
    public void shouldToPostCreateWhenTitleIsEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("");
        when(request.getParameter("content")).thenReturn("hello everyone");

        result = postController.processCreate(request,principal,model);
        expected= new ModelAndView("posts/create");

        assertEquals("page should jump to posts/create",expected.getViewName(),result.getViewName());

    }

    @Test
    public void shouldToPostCreateWhenContentIsEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("hello");
        when(request.getParameter("content")).thenReturn("");

        result = postController.processCreate(request,principal,model);
        expected= new ModelAndView("posts/create");

        assertEquals("page should jump to posts/create",expected.getViewName(),result.getViewName());

    }


    @Test
    public void shouldToPostCreateWhenTitleAndContentAreEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("");
        when(request.getParameter("content")).thenReturn("");

        result = postController.processCreate(request,principal,model);
        expected= new ModelAndView("posts/create");

        assertEquals("page should jump to posts/create",expected.getViewName(),result.getViewName());

    }

}
