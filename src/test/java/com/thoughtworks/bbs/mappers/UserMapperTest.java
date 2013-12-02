package com.thoughtworks.bbs.mappers;

import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;

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
public class UserMapperTest extends MapperTestBase{
    UserMapper userMapper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userMapper = (UserMapper) getSqlSession().getMapper(UserMapper.class);
    }

    @Test
    public void shouldFindAllUsers() {
        int before = userMapper.findAllUsers().size();
        User user1 = new UserBuilder().userName("username1").password("111111").build();
        User user2 = new UserBuilder().userName("username2").password("222222").build();
        userMapper.insert(user1);
        userMapper.insert(user2);
        List<User> users = userMapper.findAllUsers();
        assertThat("should find all users", users.size(), is(before + 2));

    }

}
