package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.PostMapper;
import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.util.PostBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PostServiceImplTest {
    private PostServiceImpl postService;
    private PostBuilder postBuilder;
    private PostMapper mapper;
    private SqlSession session;
    private SqlSessionFactory sessionFactory;

    @Before
    public void setup(){
        mapper = mock(PostMapper.class);
        session = mock(SqlSession.class);
        sessionFactory = mock(SqlSessionFactory.class);

        when(session.getMapper(PostMapper.class)).thenReturn(mapper);
        when(sessionFactory.openSession()).thenReturn(session);

        postService = new PostServiceImpl(sessionFactory);

        postBuilder = new PostBuilder();
        postBuilder.author("juntao").title("Introduce to TDD").content("ssss");


    }

    @Test
    public void shouldInsertDataToPostServiceWhenNewPost(){
        Post post = postBuilder.build();
        postService.save(post);

        verify(mapper).insert(post);
    }

    @Test
    public void shouldUpdateDataToPostServiceWhenExistingPost(){
        Post post = postBuilder.id(1L).build();
        postService.save(post);

        verify(mapper).update(post);
    }

    @Test
    public void shouldGetPostByAuthorName() {
        String authorName = "juntao";
        List<Post> expectedPostList = new ArrayList<Post>();
        expectedPostList.add(new Post());
        when((mapper.findMainPostByAuthorName(authorName))).thenReturn(expectedPostList);

        List<Post> returnedPostList = postService.findMainPostByAuthorName(authorName);
        verify(mapper).findMainPostByAuthorName(authorName);
        assertThat(returnedPostList, is(expectedPostList));
    }

    @Test
    public void shouldGetAllPostByMainPost() {
        Long id = 1L;

        List<Post> expectedPostList = new ArrayList<Post>();
        expectedPostList.add(new Post());
        when((mapper.findAllPostByMainPost(id))).thenReturn(expectedPostList);

        List<Post> returnedPostList = postService.findAllPostByMainPost(id);
        verify(mapper).findAllPostByMainPost(id);
        assertThat(returnedPostList, is(expectedPostList));
    }

    @Test
    public void shouldGetAllPostsOrderByTime() {
        List<Post> expectedNormalPostList = new ArrayList<Post>();
        expectedNormalPostList.add(new Post());
        expectedNormalPostList.add(new Post());
        List<Post> expectedTopmostPostList = new ArrayList<Post>();
        expectedTopmostPostList.add(new Post());
        when(mapper.findAllNormalPostsOrderByTime()).thenReturn(expectedNormalPostList);
        when(mapper.findAllTopmostPostsOrderByTime()).thenReturn(expectedTopmostPostList);

        List<Post> expectedAllPostList = new ArrayList<Post>();
        expectedAllPostList.addAll(expectedTopmostPostList);
        expectedAllPostList.addAll(expectedNormalPostList);

        List<Post> returnedPostList = postService.findAllPostsOrderByTime();

        verify(mapper).findAllNormalPostsOrderByTime();
        verify(mapper).findAllTopmostPostsOrderByTime();
        assertThat(returnedPostList, is(expectedAllPostList));
    }

    @Test
    public void shouldGetMyPostsOrderedByTime()
    {
        String authorName = "juntao";
        List<Post> expectedPostList = new ArrayList<Post>();
        Date date_1 = new Date();
        date_1.setTime(1);
        Date date_2 = new Date();
        date_2.setTime(100000);
        Post post1 = new Post().setAuthorName(authorName).setTitle("1").setContent("content1").setCreateTime(date_1)
                .setModifyTime(new Date()).setCreatorId(1L).setModifierId(1L).setParentId(0);
        Post post2 = new Post().setAuthorName(authorName).setTitle("2").setContent("content2").setCreateTime(date_2)
                .setModifyTime(new Date()).setCreatorId(1L).setModifierId(1L).setParentId(0);
        expectedPostList.add(post2);
        expectedPostList.add(post1);
        when((mapper.findMainPostByAuthorName(authorName))).thenReturn(expectedPostList);

        List<Post> returnedPostList = postService.findMainPostByAuthorNameSortedByCreateTime(authorName);
        assertThat(returnedPostList, is(expectedPostList));
        verify(mapper).findMainPostByAuthorName(authorName);
    }

    @Test
    public void shouldGetPostIDbyNameAndTime()
    {
        Date date_1 = new Date();
        date_1.setTime(1);
        Post post1 = new Post().setAuthorName("juntao").setTitle("1").setContent("content1").setCreateTime(date_1)
                .setModifyTime(new Date()).setCreatorId(1L).setModifierId(1L).setParentId(0);
        when(mapper.getPostIDByNameAndTime("juntao", date_1)).thenReturn(1L);

        Long result = postService.getPostIdByAuthorAndCreateTime("juntao", date_1);
        assertThat(result, is(1L));
        verify(mapper).getPostIDByNameAndTime("juntao", date_1);

    }

    @Test
    public void shouldGetSearchResult(){

        List<Post> expectedPostList = new ArrayList<Post>();
        expectedPostList.add(new Post());
        when(mapper.searchPost("%huan%","%TDD%","%TDD%","1992-10-11","5555-12-19")).thenReturn(expectedPostList);
        List<Post> returnedPostList = postService.searchPost("huan","TDD","TDD","1992-10-11","5555-12-18");
        verify(mapper).searchPost("%huan%","%TDD%","%TDD%","1992-10-11","5555-12-19");
        assertThat(returnedPostList, is(expectedPostList));

    }

    @Test
    public void shouldUpdatePostWhenSetTopmost(){
        Post post = new Post();
        when(mapper.get(Long.parseLong("123"))).thenReturn(post);

        postService.setTopMostPost("123");

        verify(mapper).update(post);
    }
}
