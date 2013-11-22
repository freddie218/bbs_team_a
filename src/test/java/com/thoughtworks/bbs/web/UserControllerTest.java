package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.PostBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import sun.security.acl.PrincipalImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserService service;
    private PostService postService;//me
    private UserController userController;
    private HttpServletRequest request;

    private Principal principal;
    private User user;
    private Post post;
    private ModelAndView expected;
    private ModelAndView result;
    private Map<String, User> map;
    private List<Post> postList;

    @Before
    public void setup() {
        service = mock(UserServiceImpl.class);
        postService = mock(PostService.class);
        request = mock(HttpServletRequest.class);

        user = new User();
        user.setUserName("username");
        user.setPasswordHash("123456");

        post = new Post();
        post.setAuthorName("username").setTitle("username").setContent("username");

        postList = new ArrayList<Post>();
        postList.add(post);

        principal = new PrincipalImpl("username");

        when(service.getByUsername("username")).thenReturn(user);
        when(postService.findMainPostByAuthorName("username")).thenReturn(postList);
        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getParameter("confirmPassword")).thenReturn("12345678");

        userController = new UserController(service);
        userController.setPostService(postService);
        map = new HashMap<String, User>();
        map.put("user", user);

    }

    @Test
    public void shouldJumpToRegisterWhenUrlMatchCreate() {
        result = userController.registerUser(new ModelMap());
        expected = new ModelAndView("user/register");

        assertEquals("Page should jump to user/register page", expected.getViewName(), result.getViewName());
        assertEquals("view should be equal", expected.getView(), result.getView());
        assertEquals("model should be equal", expected.getModel(), result.getModel());
    }

    @Test
    public void shouldJumpToProfileWhenUrlMatchProfile() {
        ModelMap model = new ModelMap();
        model.addAttribute("myPosts", postList);

        result = userController.userProfile(model,principal);
        expected = new ModelAndView("user/profile", map);

        assertEquals("page should jump to user/profile", expected.getViewName(), result.getViewName());
        assertEquals("view should be equal", expected.getView(), result.getView());
        assertEquals("model should be equal", expected.getModel(), result.getModel());
    }

    @Test
    public void shouldJumpToChangePasswordWhenUrlMatchChangePassword() {

        result = userController.changePassword(new ModelMap(), principal);
        expected = new ModelAndView("user/changePassword", map);

        assertEquals("page should jump to user/changePassword", expected.getViewName(), result.getViewName());
        assertEquals("view should be equal", expected.getView(), result.getView());
        assertEquals("model should be equal", expected.getModel(), result.getModel());
    }

    @Test
    public void shouldJumpToCreateSuccessAfterCreateNew() {
        Throwable th = null;

        expected = new ModelAndView("user/createSuccess", map);
        try {
            result = userController.processCreate(request);
        } catch (IOException e) {
            th = e;
        }

        assertEquals("page should jump to user/createSuccess", expected.getViewName(), result.getViewName());
        assertEquals("view should be equal", expected.getView(), result.getView());
        assertEquals("model should be equal", expected.getModel(), result.getModel());
        assertNull("There should be no exceptions.", th);
    }

    @Test
    public void shouldJumpToProfileAfterPasswordChange() {
        Throwable th = null;
        ModelMap model = new ModelMap();
        model.addAttribute("success", "Password changed successfully.");

        expected = new ModelAndView("user/profile", map);
        try {
            result = userController.processPasswordChange(request, principal, model);
        } catch (IOException e) {
           th = e;
        }

        assertEquals("page should jump to user/createSuccess", expected.getViewName(), result.getViewName());
        assertEquals("view should be equal", expected.getView(), result.getView());
        assertEquals("model should be equal", expected.getModel(), result.getModel());

        assertNull("There should be no exceptions.", th);

        model.clear();
        model.addAttribute("error", "Failed to change password.");

        expected = new ModelAndView("user/profile", map);
        try {
            result = userController.processPasswordChange(request, principal, model);
        } catch (IOException e) {
            th = e;
        }

        assertEquals("page should jump to user/createSuccess", expected.getViewName(), result.getViewName());
        assertEquals("view should be equal", expected.getView(), result.getView());
        assertEquals("model should be equal", expected.getModel(), result.getModel());
        assertNull("There should be no exceptions.", th);
    }
}
