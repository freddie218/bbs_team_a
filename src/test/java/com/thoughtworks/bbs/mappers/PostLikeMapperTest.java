package com.thoughtworks.bbs.mappers;


import com.thoughtworks.bbs.model.PostLike;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PostLikeMapperTest extends MapperTestBase{
    PostLikeMapper postLikeMapper;
    PostLike postLike;

    @Before
    public void setup() throws Exception {
        super.setUp();
        postLikeMapper = getSqlSession().getMapper(PostLikeMapper.class);
        postLike = new PostLike().setPostID(1L).setUserID(10L);
    }


    @Test
    public void shouldGetAllPostLike()
    {
        int before = postLikeMapper.getAll().size();
        PostLike one = new PostLike().setPostID(3L).setUserID(11L);
        PostLike two = new PostLike().setPostID(5L).setUserID(12L);
        postLikeMapper.insert(one);
        postLikeMapper.insert(two);
        assertEquals(postLikeMapper.getAll().size(), before + 2);
    }

    @Test
    public void shouldInsertNewLike()
    {
        int before = postLikeMapper.getAll().size();
        PostLike newPostLike = new PostLike().setPostID(1L).setUserID(11L);
        postLikeMapper.insert(newPostLike);
        assertEquals(postLikeMapper.getAll().size(), before + 1);
    }

    @Test
    public void shouldGetAllPostLikeByUserID()
    {
        int before = postLikeMapper.getPostLikeByUserID(10L).size();
        PostLike one = new PostLike().setPostID(2L).setUserID(10L);
        PostLike two = new PostLike().setPostID(3L).setUserID(10L);
        postLikeMapper.insert(one);
        postLikeMapper.insert(two);
        List<PostLike> result = postLikeMapper.getPostLikeByUserID(10L);
        assertEquals(result.size(), before + 2);
    }

    @Test
    public void shouldGetAllPostLikeByPostID()
    {
        int before = postLikeMapper.getPostLikeByPostID(1L).size();
        PostLike one = new PostLike().setPostID(1L).setUserID(11L);
        PostLike two = new PostLike().setPostID(1L).setUserID(12L);
        postLikeMapper.insert(one);
        postLikeMapper.insert(two);
        List<PostLike> result = postLikeMapper.getPostLikeByPostID(1L);
        assertEquals(result.size(), before + 2);
    }


    @Test
    public void shouldDeleteLikePost()
    {
        int before = postLikeMapper.getAll().size();
        PostLike newPostLike = new PostLike().setUserID(10L).setPostID(20L);
        postLikeMapper.insert(newPostLike);
        postLikeMapper.delete(newPostLike);
        int result = postLikeMapper.getAll().size();
        assertEquals(result, before);
    }
}
