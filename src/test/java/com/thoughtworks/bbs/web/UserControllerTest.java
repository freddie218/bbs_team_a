package com.thoughtworks.bbs.web;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.model.UserRole;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.UserService;
import com.thoughtworks.bbs.service.impl.UserServiceImpl;
import com.thoughtworks.bbs.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UserControllerTest {
    private UserService userService;
    private PostService postService;
    private UserController userController;
    private HttpServletRequest request;

    private Principal principal;
    private User user;
    private Post post;
    private ModelAndView expected;
    private ModelAndView result;
    private List<Post> postList;
    private UserRole userRole;

    @Before
    public void setup() {
        userService = mock(UserServiceImpl.class);
        postService = mock(PostService.class);
        principal = mock(Principal.class);
        request = mock(HttpServletRequest.class);

        user = new User();
        user.setUserName("username");
        user.setPasswordHash("123456");
        user.setId(1L);

        userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleName("ROLE_REGULAR");

        post = new Post();
        post.setAuthorName("username").setTitle("username").setContent("username");

        postList = new ArrayList<Post>();
        postList.add(post);

        when(principal.getName()).thenReturn("username");
        when(userService.getByUsername("username")).thenReturn(user);
        when(postService.findMainPostByAuthorName("username")).thenReturn(postList);

        userController = new UserController().setUserService(userService).setPostService(postService);
    }

    @Test
    public void shouldJumpToRegisterWhenUrlMatchCreate() {
        result = userController.registerUser(new ModelMap());
        expected = new ModelAndView("user/register");

        assertEquals("Page should jump to user/register page", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToProfileWhenUrlMatchProfile() {
        ModelMap model = new ModelMap();

        result = userController.userProfile(model,principal);
        expected = new ModelAndView("user/profile");

        assertEquals("page should jump to user/profile", expected.getViewName(), result.getViewName());

        verify(postService).findMainPostByAuthorNameSortedByCreateTime(principal.getName());
    }

    @Test
    public void shouldJumpToChangePasswordWhenUrlMatchChangePassword() {

        result = userController.changePassword(new ModelMap(), principal);
        expected = new ModelAndView("user/changePassword");

        assertEquals("page should jump to user/changePassword", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToShowUsersWhenUrlMatchUsers() {

        result = userController.showAllUsers(new ModelMap());
        expected = new ModelAndView("user/users");

        assertEquals("page should jump to user/users", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToCreateSuccessAfterCreateNew() throws IOException {
        Throwable th = null;

        expected = new ModelAndView("user/createSuccess");
        result = userController.processCreate(request);

        assertEquals("page should jump to user/createSuccess", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToProfileAfterPasswordChangeSuccess() throws IOException {
        Throwable th = null;
        ModelMap model = new ModelMap();
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getParameter("confirmPassword")).thenReturn("1234567");
        when(userService.passwordVerify(user, "123456")).thenReturn(true);
        when(userService.password(user, "1234567")).thenReturn(true);


        expected = new ModelAndView("user/profile");
        result = userController.processPasswordChange(request, principal, model);

        verify(userService).password(user, "1234567");
        assertEquals("page should jump to user/profile", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToProfileAfterPasswordChangeFailed() throws IOException {
        Throwable th = null;
        ModelMap model = new ModelMap();
        when(request.getParameter("password")).thenReturn("12345678");
        when(request.getParameter("confirmPassword")).thenReturn("1234567");
        when(userService.passwordVerify(user, "12345678")).thenReturn(false);

        expected = new ModelAndView("user/profile");
        result = userController.processPasswordChange(request, principal, model);

        verify(userService, times(0)).password(argThat(new UserMatcher()), eq("1234567"));
        assertEquals("page should jump to user/profile", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToUpdateProfileWhenUrlMatchUpdateUsername() {

        result = userController.changeUsername(new ModelMap(), principal);
        expected = new ModelAndView("user/updateProfile");

        assertEquals("page should jump to user/updateProfile", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToProfileAfterUpdateUsernameSuccess() throws IOException {
        ModelMap model = new ModelMap();
        User newUser = new User();
        newUser.setUserName("newname");
        newUser.setPasswordHash(user.getPasswordHash());

        when(request.getParameter("newUsername")).thenReturn("newname");
        when(userService.getByUsername("newname")).thenReturn(null);
        when(userService.getByUsername("username")).thenReturn(user);
        when(postService.findMainPostByAuthorNameSortedByCreateTime("username")).thenReturn(postList);


        expected = new ModelAndView("user/profile");
        result = userController.processUpdateUsername(request,model,principal);

        verify(postService).save(post);
        verify(userService).update(argThat(new IsSameUserWith(newUser)));
        assertEquals("page should jump to user/profile", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldStayUpdateProfileWhenNewUsernameHasAlreadyExist() throws IOException {
        ModelMap model = new ModelMap();
        User existUser = new User();
        existUser.setUserName("newname");
        existUser.setPasswordHash(user.getPasswordHash());

        when(request.getParameter("newUsername")).thenReturn("newname");
        when(userService.getByUsername("newname")).thenReturn(existUser);
        when(postService.findMainPostByAuthorNameSortedByCreateTime("username")).thenReturn(postList);


        expected = new ModelAndView("user/updateProfile");
        result = userController.processUpdateUsername(request,model,principal);

        verify(userService, never()).update(argThat(new IsSameUserWith(user)));
        verify(postService,never()).save(post);
        assertEquals("page should stay user/updateProfile", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldStayUpdateProfileWhenNewUsernameIsEmpty() throws IOException {
        ModelMap model = new ModelMap();
        User existUser = new User();

        when(request.getParameter("newUsername")).thenReturn("");
        when(userService.getByUsername("")).thenReturn(null);
        result = userController.processUpdateUsername(request,model,principal);

        expected = new ModelAndView("user/updateProfile");
        result = userController.processUpdateUsername(request,model,principal);

        verify(userService, never()).update(argThat(new IsSameUserWith(user)));
        verify(postService,never()).save(post);
        assertEquals("page should stay user/updateProfile", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldDeleteThePost() {
        Post aPost = new Post();
        aPost.setPostId(1L);
        when(postService.get(1L)).thenReturn(aPost);
        when(request.getParameter("deletePost")).thenReturn("1");

        expected = new ModelAndView("user/profile");
        result = userController.processDeletePost(request, principal, new ModelMap());

        verify(postService).delete(aPost);
        verify(postService).findMainPostByAuthorNameSortedByCreateTime(principal.getName());


    }

    @Test
    public void shouldAuthoriseUser (){
        expected = new ModelAndView("user/users");
        when(request.getParameter("authoriseUserId")).thenReturn("1");
        result = userController.authoriseUser(request,new ModelMap());

        verify(userService).authoriseUser(1L);
        assertEquals("page should stay user/users", expected.getViewName(), result.getViewName());
    }

    @Test
    public void shouldJumpToUsersAfterDisableUserSuccess(){
        expected = new ModelAndView("redirect:users");
        result = userController.processUserDisable(request, new ModelMap());

        verify(userService).disable(any(User.class));
        assertEquals("page should be at user/users", expected.getViewName(), result.getViewName());

    }

    private class UserMatcher extends ArgumentMatcher<User> {

        @Override
        public boolean matches(Object arg) {
            if( null == arg || !(arg instanceof User) )
                 return  false;
            return ((User) arg).getUserName().equals("username");
         }
    }

    class IsSameUserWith extends ArgumentMatcher<User> {
        private User user;

        IsSameUserWith(User user) {
            this.user = user;
        }

        @Override
        public boolean matches(Object userToMatch) {
            return ((User) userToMatch).getUserName().equals(user.getUserName())
                    && ((User) userToMatch).getPasswordHash().equals(user.getPasswordHash());
        }
    }

    @Test
    public void shouldShowUserProfileWhenClickPostAuthorName() {
        Long id = 22L;
        when(principal.getName()).thenReturn("loginhuan");
        when(userService.get(id)).thenReturn(new UserBuilder().id(22L).userName("yj").password("11").build());
        ModelMap model = new ModelMap();
        ModelAndView result = userController.showUserProfile(id, principal, model);
        expected = new ModelAndView("user/profile");
        assertThat("should show user profile when click post author name",result.getViewName(), is(expected.getViewName()));


    }

    @Test
    public void shouldDeletePostWhenClickDeleteAndUserIsShowUser() {
        Post aPost = new Post();
        aPost.setPostId(1L);
        when(postService.get(1L)).thenReturn(aPost);
        when(request.getParameter("deletePost")).thenReturn("1");
        expected = new ModelAndView("user/profile");
        result = userController.DeletePost(request, principal, new ModelMap());

        verify(postService).delete(aPost);
        verify(postService).findMainPostByAuthorNameSortedByCreateTime(principal.getName());
    }

}
