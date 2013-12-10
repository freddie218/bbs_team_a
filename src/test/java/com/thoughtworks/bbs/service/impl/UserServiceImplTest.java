package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.UserMapper;
import com.thoughtworks.bbs.mappers.UserRoleMapper;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.model.UserRole;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private User user;
    private UserMapper userMapper;
    private SqlSession session;
    private SqlSessionFactory sessionFactory;
    private UserRoleMapper userRoleMapper;
    private UserRole userRole;

    @Before
    public void setup(){
        session = mock(SqlSession.class);
        userMapper = mock(UserMapper.class);
        userRoleMapper = mock(UserRoleMapper.class);

        sessionFactory = mock(SqlSessionFactory.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.getMapper(UserMapper.class)).thenReturn(userMapper);
        when(session.getMapper(UserRoleMapper.class)).thenReturn(userRoleMapper);

        userService = new UserServiceImpl(sessionFactory);

        user = new User();
        user.setUserName("user");
        user.setPasswordHash("password");
        user.setId(1L);

        userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleName("ROLE_REGULAR");

    }

    @Test
    public void shouldInsertUserWhenCreateNew(){
        userService.save(user);

        verify(userMapper).insert(user);
    }

    @Test
    public void shouldUpdateUserInfoWhenUpdate(){
        userService.update(user);

        verify(userMapper).update(user);
    }

    @Test
    public void shouldShowAllUsers(){
        userService.getAll();

        verify(userMapper).findAllUsers();
    }

    @Test
    public void shouldReturnFalseWhenPasswordIncorrect() {
        String wrongPassword = "xxxxxxxxxx";
        assertEquals(userService.passwordVerify(user, wrongPassword), false);
    }

    @Test
    public void shouldReturnTrueWhenPasswordCorrect() {
        String password = user.getPasswordHash();
        assertEquals(userService.passwordVerify(user, password), true);
    }

    @Test
    public void shouldChangeUserPasswordWhenAsked() {
        User updatedUser = new User();
        updatedUser.setUserName(user.getUserName());
        updatedUser.setPasswordHash("newpass");
        when(userMapper.findByUsername(user.getUserName())).thenReturn(updatedUser);

        boolean ret = userService.password(user, "newpass");
        verify(userMapper).update(user);
        assertEquals(userService.getByUsername(user.getUserName()).getPasswordHash(), "newpass");
        assertEquals(ret, true);
    }

    @Test
    public void shouldReturnUserWhenGetUserId() {
        Long userId = 1L;
        userService.get(userId);
        verify(userMapper).findByUserId(userId);
    }

    @Test
    public void  shouldReturnAllUsersWhithRoleWhenInvoked(){
        List<User> users = new ArrayList<User>();
        users.add(user);

        when(userMapper.findAllUsers()).thenReturn(users);
        when(userRoleMapper.get(user.getId())).thenReturn(userRole);

        Map<User,String> result = userService.getAllUsersWithRole();
        Map<User,String> expect = new HashMap<User, String>();
        expect.put(user,userRole.getRoleName());
        assertEquals(result,expect);
        verify(userMapper).findAllUsers();
        verify(userRoleMapper).get(user.getId());
    }

    @Test
    public void shouldRuturnUserRoleWhenInvoked(){
        userService.getUserRoleById(user.getId());

        verify(userRoleMapper).get(user.getId());
    }

    @Test
    public void shouldUpdateUserRoleWhenInvokeUpdateUserRole(){
        userService.updateUserRole(userRole);

        verify(userRoleMapper).update(userRole);
    }

    @Test
    public void shouldUpdateUserRoleWhenInvokeAuthoriseUser(){
        UserRole updatedUserRole = new UserRole();
        updatedUserRole.setUserId(1L);
        updatedUserRole.setRoleName("ROLE_ADMIN");

        when(userRoleMapper.get(userRole.getUserId())).thenReturn(userRole);

        userService.authoriseUser(userRole.getUserId());
        verify(userRoleMapper).get(userRole.getUserId());
        verify(userRoleMapper).update(argThat(new  UserRoleMatcher(updatedUserRole)));
    }

    class UserRoleMatcher extends ArgumentMatcher<UserRole> {
        private UserRole userRole;

        UserRoleMatcher(UserRole userRole) {
            this.userRole = userRole;
        }

        @Override
        public boolean matches(Object userroleToMatch) {
            return ((UserRole) userroleToMatch).getUserId().equals(userRole.getUserId())
                    && ((UserRole) userroleToMatch).getRoleName().equals(userRole.getRoleName());
        }
    }

    @Test
    public void  shouldReturnTrueWhenUserDisabledSuccess() {
        user.setEnabled(true);

        User disabledUser = new User();
        disabledUser.setEnabled(false);
        disabledUser.setUserName("user");
        when(userMapper.findByUsername("user")).thenReturn(disabledUser);

        assertTrue(userService.disable(user));
        verify(userMapper).update(user);
        assertFalse(userService.getByUsername("user").isEnabled());
    }

    @Test
    public void  shouldReturnFalseWhenUserDisabledFailed() {
        user.setEnabled(false);

        assertFalse("should return false when given null user", userService.disable(null));
        assertFalse("should return false when given disabled user", userService.disable(user));
        verify(userMapper, never()).update(user);
    }

}
