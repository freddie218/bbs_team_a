package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.PostBuilder;
import com.thoughtworks.bbs.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-20
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
public class HomeControllerTest {
    private PostService service;
    private UserService userService;
    private HomeController controller;
    private Principal principal;
    private Model model;
    PostBuilder postBuilder;
    UserBuilder  userBuilder;

    @Before
    public void setup() {
        service = mock(PostServiceImpl.class);
        userService = mock(UserServiceImpl.class);
        controller = new HomeController(service,userService);

        model = new ExtendedModelMap();
        userBuilder = new UserBuilder();
        userBuilder.userName("username").password("123456");

        postBuilder = new PostBuilder();
        postBuilder.author("jwx").title("newPost").content("www");
    }

    @Test
    public void shouldReturnLoginWhenGetPrincipalNull() {
        principal = null;
        String expectedUrl = "login";
        Model expectedModel = new ExtendedModelMap();

        String resultUrl = controller.get(model, postBuilder.build(), principal);
        assertThat(resultUrl, is(expectedUrl));
        assertThat(model, is(expectedModel));
    }

    @Test
    public void  shouldReturnHomeWhenGetPrincipalNotNull() {
        principal = new PrincipalImpl(userBuilder.build().getUserName());
        List<Post> expectedPosts = new ArrayList<Post>();
        expectedPosts.add(new Post().setAuthorName("huan"));
        when(service.findAllPostsOrderByTime()).thenReturn(expectedPosts);
        User user = new UserBuilder().userName("huan").build();
        when(userService.getByUsername("huan")).thenReturn(user);
        List<User> users = new ArrayList<User>();
        users.add(user);
        String expectedUrl = "home";
        Model expectedModel = new ExtendedModelMap();
        expectedModel.addAttribute("posts", expectedPosts);
        expectedModel.addAttribute("users", users);

        String resultUrl = controller.get(model, postBuilder.build(), principal);
        verify(service).findAllPostsOrderByTime();
        assertThat(resultUrl, is(expectedUrl));
        assertThat(model, is(expectedModel));
    }





}
