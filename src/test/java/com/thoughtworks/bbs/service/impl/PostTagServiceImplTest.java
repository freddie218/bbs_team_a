package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.PostTagsMapper;
import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.PostTag;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostTagServiceImplTest {
    private PostTagServiceImpl postTagService;
    private PostTagsMapper postTagsMapper;
    private SqlSession sqlSession;
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void setup()
    {
        postTagsMapper = mock(PostTagsMapper.class);
        sqlSession = mock(SqlSession.class);
        sqlSessionFactory = mock(SqlSessionFactory.class);

        when(sqlSession.getMapper(PostTagsMapper.class)).thenReturn(postTagsMapper);
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);

        postTagService = new PostTagServiceImpl(sqlSessionFactory);
    }

    @Test
    public void shouldGetAllPostTagByATag()
    {
        String tag = "one";
        List<PostTag> expected = new ArrayList<PostTag>();
        expected.add(new PostTag());
        when(postTagsMapper.getPostTagsByATag(tag)).thenReturn(expected);

        List<PostTag> result = postTagService.getPostTagsByATag(tag);
        assertThat(result, is(expected));

    }

    @Test
    public void shouldGetAllPostTagByPostID()
    {
        Long postID = 1L;
        PostTag onePostTag = new PostTag().setATag("one").setPostID(postID);
        List<PostTag> expected = new ArrayList<PostTag>();
        expected.add(onePostTag);
        when(postTagsMapper.getPostTagsByPostID(postID)).thenReturn(expected);

        List<PostTag> result = postTagService.getPostTagByPostID(postID);
        verify(postTagsMapper).getPostTagsByPostID(postID);
        assertThat(result, is(expected));
    }

    @Test
    public void shouldSaveOnePostTag()
    {
        PostTag postTag = new PostTag().setATag("one").setPostID(1L);
        postTagService.save(postTag);
        verify(postTagsMapper).insert(postTag);
    }

    @Test
    public void shouldDeleteOnePostTag()
    {
        PostTag postTag = new PostTag();
        postTagService.save(postTag);
        postTagService.deletePostTag(postTag);
        verify(postTagsMapper).delete(postTag);
    }
}
