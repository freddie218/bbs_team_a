package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.PostLike;
import com.thoughtworks.bbs.model.PostTag;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.service.PostLikeService;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.PostTagService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.PostServiceImpl;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.PostBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
    private PostTagService postTagService;
    private PostController postController;
    private HttpServletRequest request;
    private User user;

    private Principal principal;
    private RedirectAttributesModelMap model;
    private ModelMap modelMap;
    private ModelAndView expected;
    private ModelAndView result;

    @Before
    public void setup(){
        postService = mock(PostServiceImpl.class);
        userService = mock(UserServiceImpl.class);
        postLikeService = mock(PostLikeService.class);
        postTagService = mock(PostTagService.class);
        request = mock(HttpServletRequest.class);
        principal = mock(Principal.class);

        user = new User();
        user.setUserName("name");
        user.setPasswordHash("00000");
        user.setId(0L);

        when(userService.getByUsername("name")).thenReturn(user);
        when(request.getParameter("parentId")).thenReturn("0");
        when(principal.getName()).thenReturn("name") ;

        model = new RedirectAttributesModelMap();
        modelMap = new ModelMap();

        postController = new PostController();
        postController.setPostService(postService);
        postController.setUserService(userService);
        postController.setPostLikeService(postLikeService);
        postController.setPostTagService(postTagService);

    }

    @Test
    public void shouldJumpToHomeWhenCreatePostSuccess() throws IOException {

        when(request.getParameter("title")).thenReturn("hello");
        when(request.getParameter("content")).thenReturn("hello everyone");
        when(request.getParameter("allTags")).thenReturn("tag1;tag2,tag3");

        result = postController.processCreate(request, principal, modelMap);
        expected = new ModelAndView("redirect:/");

        assertEquals("page should jump to home",expected.getViewName(),result.getViewName());
    }

    @Test
    public void shouldToPostCreateWhenTitleIsEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("");
        when(request.getParameter("content")).thenReturn("hello everyone");

        result = postController.processCreate(request, principal, modelMap);
        expected= new ModelAndView("posts/create");

        assertEquals("page should jump to posts/create", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldToPostCreateWhenContentIsEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("hello");
        when(request.getParameter("content")).thenReturn("");

        result = postController.processCreate(request, principal, modelMap);
        expected= new ModelAndView("posts/create");

        assertEquals("page should jump to posts/create", expected.getViewName(), result.getViewName());
    }


    @Test
    public void shouldToPostCreateWhenTitleAndContentAreEmpty() throws IOException {

        when(request.getParameter("title")).thenReturn("");
        when(request.getParameter("content")).thenReturn("");

        result = postController.processCreate(request, principal, modelMap);
        expected= new ModelAndView("posts/create");

        assertEquals("page should jump to posts/create",expected.getViewName(),result.getViewName());

    }

    @Test
    public void shouldToPostShowWhenReplyPostSuccess() throws IOException {

        when(userService.getByUsername("")).thenReturn(null);

        when(request.getParameter("title")).thenReturn("Re");
        when(request.getParameter("content")).thenReturn("OK");

        Long postId = 1L;
            result = postController.processReplyPost(postId, null,request,principal,model);
        expected = new ModelAndView("redirect:" + postId);

        assertEquals("page should stay posts/show",expected.getViewName(),result.getViewName());

    }


    @Test
    public void shouldToPostShowWhenReplyPostError() throws IOException {

        when(userService.getByUsername("")).thenReturn(null);

        when(request.getParameter("title")).thenReturn("Re");
        when(request.getParameter("content")).thenReturn("");
        Long postId = 1L;
        result = postController.processReplyPost(postId, null,request,principal,model);
        expected = new ModelAndView("redirect:" + postId);

        assertEquals("page should stay posts/show",expected.getViewName(),result.getViewName());

    }

    @Test
    public void shouldStayAndNotReplyWhenBadWordsDetectedInReply() {
        when(request.getParameter("title")).thenReturn("bal...bla...");
        when(request.getParameter("content")).thenReturn("I love hk.");

        Long postId = 1L;
        expected = new ModelAndView("redirect:" + postId);
        Post post = new PostBuilder().author("somebody").parentId(0L).content("some content.").build();
        result = postController.processReplyPost(postId, post, request,principal, model);
        assertEquals("should stay and not reply", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldStayPostCreatePageWhenBadWordsDetected() throws IOException {
        when(request.getParameter("title")).thenReturn("I love HK");
        when(request.getParameter("content")).thenReturn("bla..bla...");

        expected = new ModelAndView("posts/create");
        result = postController.processCreate(request, principal, modelMap);
        assertEquals("shoul stay while bad words detected", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldShowAllInfoAboutOnePost() {

        Post post = new PostBuilder().author("someone").id(5L).parentId(3L).build();
        postService.save(post);
        when(postService.get(post.getPostId())).thenReturn(post);

        ModelAndView expected = new ModelAndView("posts/show");
        ModelAndView result = new ModelAndView(postController.get(5L, model, new Post(), principal));

        assertEquals(expected.getViewName(), result.getViewName());

    }


    @Test
    public void shouldStayAtShowPageAfterReplyDeletion() {
        Post post = new PostBuilder().author("someone").id(5L).parentId(3L).build();
        Post parentPost = new PostBuilder().author("author").id(3L).parentId(0L).build();
        when(request.getParameter("postIdToDel")).thenReturn("5");
        when(userService.getByUsername(user.getUserName())).thenReturn(user);
        when(postService.get(5L)).thenReturn(post);
        when(postService.get(3L)).thenReturn(parentPost);
        when(postLikeService.isLiked(0L,3L)).thenReturn(true);

        expected = new ModelAndView("redirect:/posts/" + post.getParentId());
        result = postController.processPostDeletion(request, parentPost.getPostId(), model, parentPost, principal);

        verify(postService).delete(post);
        assertEquals("page should stay posts/show", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToHomePageAfterMainPostDeletion() {
        Post post = new Post();
        post.setPostId(3L);
        post.setParentId(0L);
        when(request.getParameter("postIdToDel")).thenReturn("3");
        when(postService.get(3L)).thenReturn(post);

        expected = new ModelAndView("redirect:/");
        result = postController.processPostDeletion(request, post.getPostId(), model, post, principal);

        verify(postService).delete(post);
        assertEquals("page should turn to home", expected.getViewName(), result.getViewName());
    }


    @Test
    public void shouldLikeThePost()
    {
        Post aPost = new Post().setPostId(10L).setLikeTime(0L);
        Long userID = userService.getByUsername(principal.getName()).getId();
        PostLike aPostLike = new PostLike().setUserID(userID).setPostID(10L);
        when(request.getParameter("likePost")).thenReturn("10");
        when(postService.get(10L)).thenReturn(aPost);

        result = postController.processLikePost(request, principal, model);
        expected = new ModelAndView("redirect:" + "10");

        assertEquals("page should stay posts/10:", expected.getViewName(), result.getViewName());
        verify(postService).save(argThat(new IsSamePostWith(aPost)));
        verify(postLikeService).save(argThat(new IsSamePostLikeWith(aPostLike)));
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
