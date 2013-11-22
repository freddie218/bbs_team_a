package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.UserMapper;
import com.thoughtworks.bbs.mappers.UserRoleMapper;
import com.thoughtworks.bbs.model.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private User user;
    private UserMapper userMapper;
    private SqlSession session;
    private SqlSessionFactory sessionFactory;
    private UserRoleMapper userRoleMapper;

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
    public void shouldReturnFalseWhenPasswordIncorrect_TrueWhenCorrect() {
        String password = user.getPasswordHash();
        assertEquals(userService.userVerify(user, password), true);
        String wrongPassword = "xxxxxxxxxx";
        assertEquals(userService.userVerify(user, wrongPassword), false);
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
}
