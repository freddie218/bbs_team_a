package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.PostMapper;
import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.util.PostBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
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

        postService.save( createPost("huan", "Thought in TDD class", "TDD is a test driven divelopment, that is TDD.", new Date(2013, 12, 18, 12, 31)));
        postService.save(createPost("juntao","Database migration","Database migration is not a easy thing to do.",new Date(2013,11,18,0,30)));

    }

    private Post createPost(String author, String title, String content, Date date){
        return new PostBuilder()
        .author(author)
                .title(title)
                .content(content)
                .parentId(0L)
                .createTime(date)
                .build();
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
        List<Post> expectedPostList = new ArrayList<Post>();
        expectedPostList.add(new Post());
        expectedPostList.add(new Post());
        expectedPostList.add(new Post());
        when(mapper.findAllPostsOrderByTime()).thenReturn(expectedPostList);

        List<Post> returnedPostList = postService.findAllPostsOrderByTime();

        verify(mapper).findAllPostsOrderByTime();
        assertThat(returnedPostList, is(expectedPostList));
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

    @Ignore
    public void shouldGetSearchResult(){
        List<Post> result = postService.searchPost("huan", "TDD", "", "2013-11-10", "");
        assertEquals(result.size(),1);
        result = postService.searchPost("juntao", "TDD", "","","");
        assertEquals(result.size(),0);
        result = postService.searchPost("","","","","2013-12-30");
        assertEquals(result.size(),2);
    }


}
