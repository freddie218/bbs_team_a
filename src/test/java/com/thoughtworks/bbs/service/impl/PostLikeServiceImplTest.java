package com.thoughtworks.bbs.service.impl;


import com.thoughtworks.bbs.mappers.PostLikeMapper;
import com.thoughtworks.bbs.model.PostLike;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

public class PostLikeServiceImplTest {
    private PostLikeServiceImpl postLikeService;
    private PostLikeMapper postLikeMapper;
    private SqlSession session;
    private SqlSessionFactory sessionFactory;

    @Before
    public void setup()
    {
        postLikeMapper = mock(PostLikeMapper.class);
        session = mock(SqlSession.class);
        sessionFactory = mock(SqlSessionFactory.class);

        when(session.getMapper(PostLikeMapper.class)).thenReturn(postLikeMapper);
        when(sessionFactory.openSession()).thenReturn(session);

        postLikeService = new PostLikeServiceImpl(sessionFactory);

    }

    @Test
    public void shouldGetAllPostLikeByUserID()
    {
        Long user_id = 1L;
        List<PostLike> expected = new ArrayList<PostLike>();
        expected.add(new PostLike());
        when(postLikeMapper.getPostLikeByUserID(user_id)).thenReturn(expected);

        List<PostLike> result = postLikeService.getPostLikeByUserID(user_id);
        verify(postLikeMapper).getPostLikeByUserID(user_id);
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetAllPostLikeByPostID()
    {
        Long post_id = 1L;
        List<PostLike> expected = new ArrayList<PostLike>();
        expected.add(new PostLike());
        when(postLikeMapper.getPostLikeByPostID(post_id)).thenReturn(expected);

        List<PostLike> result = postLikeService.getPostLikeByPostID(post_id);
        verify(postLikeMapper).getPostLikeByPostID(post_id);
        assertThat(result, is(expected));
    }

    @Test
    public void shouldInsertNewPostLike()
    {
        PostLike newPostLike = new PostLike().setUserID(1L).setPostID(10L);
        postLikeService.save(newPostLike);
        verify(postLikeMapper).insert(newPostLike);
    }

    @Test
    public void shouldReturnTrueWhenLiked()
    {
        PostLike newPostLike = new PostLike().setPostID(1L).setUserID(10L);
        List<PostLike> aList = new ArrayList<PostLike>();
        aList.add(newPostLike);
        when(postLikeMapper.getPostLikeByUserID(10L)).thenReturn(aList);

        postLikeService.save(newPostLike);

        boolean expected = true;
        boolean result = postLikeService.isLiked(10L, 1L);

        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnFalseWhenNotLiked()
    {
        PostLike newPostLike = new PostLike().setPostID(1L).setUserID(10L);
        List<PostLike> aList = new ArrayList<PostLike>();
        aList.add(newPostLike);
        when(postLikeMapper.getPostLikeByUserID(10L)).thenReturn(aList);

        postLikeService.save(newPostLike);

        boolean expected = false;
        boolean result = postLikeService.isLiked(11L, 1L);

        assertEquals(expected, result);
    }

    @Test
    public void shouldDeletePostLike()
    {
        PostLike aPostLike = new PostLike().setUserID(1L).setUserID(10L);
        postLikeService.save(aPostLike);

        postLikeService.deletePostLike(aPostLike);
        verify(postLikeMapper).delete(aPostLike);
    }
}
