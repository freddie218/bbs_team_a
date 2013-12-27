package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.PostLike;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostLikeService;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.PostBuilder;
import com.thoughtworks.bbs.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private PostLikeService postLikeService;
    PostBuilder postBuilder;
    UserBuilder  userBuilder;
    private HttpServletRequest request;
    private User user;

    @Before
    public void setup() {
        service = mock(PostServiceImpl.class);
        userService = mock(UserServiceImpl.class);
        postLikeService = mock(PostLikeService.class);
        controller = new HomeController(service, userService, postLikeService);
        request = mock(HttpServletRequest.class);
        principal = mock(Principal.class);

        model = new ExtendedModelMap();
        userBuilder = new UserBuilder();
        userBuilder.userName("username").password("123456").id(1L);

        user = userBuilder.build();

        postBuilder = new PostBuilder();
        postBuilder.author("username").title("newPost").content("www");
        when(principal.getName()).thenReturn("username");
    }

    @Test
    public void shouldReturnHomeAfterSetTopmost(){
        when(request.getParameter("postIdToTop")).thenReturn("123");

        ModelAndView result = controller.setTopMost(request,principal,model);

        verify(service).setTopMostPost("123");
        assertEquals("redirect:/", result.getViewName());
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
    public void shouldLikeAPost() {

        when(userService.getByUsername("username")).thenReturn(user);
        when(request.getParameter("likePost")).thenReturn("10");
        Post aPost = new Post().setPostId(10L).setLikeTime(0L);

        Long userID = userService.getByUsername(principal.getName()).getId();
        PostLike aPostLike = new PostLike().setUserID(userID).setPostID(10L);
        when(service.get(10L)).thenReturn(aPost);

        ModelAndView result = controller.likeProcess(request, principal, model);
        ModelAndView expected = new ModelAndView("home");

        assertEquals(expected.getViewName(), result.getViewName());
        verify(service).save(argThat(new IsSamePostWith(aPost)));
        verify(postLikeService).save(argThat(new IsSamePostLikeWith(aPostLike)));
    }

    @Test
    public void shouldShowSearchResult(){
        when(userService.getByUsername("username")).thenReturn(user);
        ModelAndView result = controller.searchPost(request,principal, new ModelMap());
        ModelAndView expected = new ModelAndView("home");
        assertEquals(expected.getViewName(), result.getViewName());

    }

    @Test
    public void shouldLikeSearchPost(){
        when(userService.getByUsername("username")).thenReturn(user);
        when(request.getParameter("likePost")).thenReturn("10");
        Post aPost = new Post().setPostId(10L).setLikeTime(0L);
        Long userID = userService.getByUsername(principal.getName()).getId();
        PostLike aPostLike = new PostLike().setUserID(userID).setPostID(10L);
        when(service.get(10L)).thenReturn(aPost);
        ModelAndView result = controller.searchLike(request,principal, new ModelMap());
        ModelAndView expected = new ModelAndView("home");
        assertEquals(expected.getViewName(), result.getViewName());
    }


    class IsSamePostWith extends ArgumentMatcher<Post> {
        private Post post;

        IsSamePostWith(Post post) {
            this.post = post;
        }

        @Override
        public boolean matches(Object postToMatch) {
            return ((Post) postToMatch).getPostId().equals(post.getPostId());
        }
    }

    class IsSamePostLikeWith extends ArgumentMatcher<PostLike> {
        private PostLike postLike;

        IsSamePostLikeWith(PostLike postLike) {
            this.postLike = postLike;
        }

        @Override
        public boolean matches(Object postLikeToMatch) {
            return ((PostLike) postLikeToMatch).getPost_id().equals(postLike.getPost_id())
                    &&((PostLike) postLikeToMatch).getUser_id().equals(postLike.getUser_id());
        }
    }





}
