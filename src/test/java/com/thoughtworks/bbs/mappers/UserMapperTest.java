package com.thoughtworks.bbs.mappers;

import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.util.UserBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:persistence-config.xml"})
@Transactional
public class UserMapperTest{
    @Autowired
    UserMapper userMapper;

    @Test
    public void shouldFindAllUsers() {
        User user1 = new UserBuilder().userName("username1").password("111111").build();
        User user2 = new UserBuilder().userName("username2").password("222222").build();
        userMapper.insert(user1);
        userMapper.insert(user2);
        List<User> users = userMapper.findAllUsers();
        assertThat("should find all users", users.size(), is(2));

    }

    @Test
    public void shouldFindUserById() {
        userMapper.insert(new UserBuilder().userName("juntao").id(2L).password("123456").build());
        User user = userMapper.findByUsername("juntao");
        assertThat("should find user by user id", true,
              is(new IsSameUserWith(userMapper.findByUserId(user.getId())).matches(user)));
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
}
