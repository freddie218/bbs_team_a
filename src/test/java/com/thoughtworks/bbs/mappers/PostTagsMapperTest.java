package com.thoughtworks.bbs.mappers;

import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.PostLike;
import com.thoughtworks.bbs.model.PostTag;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PostTagsMapperTest extends MapperTestBase {
    PostTagsMapper postTagsMapper;
    PostTag postTag;

    @Before
    public void setup() throws Exception {
        super.setUp();
        postTagsMapper = getSqlSession().getMapper(PostTagsMapper.class);
        postTag = new PostTag().setPostID(1L).setATag("Tag1");
    }

    @Test
    public void shouldInsertOnePostTag() {
        int before = postTagsMapper.getAll().size();
        postTagsMapper.insert(new PostTag().setPostID(1L).setATag("aTag"));
        assertEquals(postTagsMapper.getAll().size(), before + 1);
    }

    @Test
    public void shouldDeleteOnePostTag() {
        int before = postTagsMapper.getAll().size();
        PostTag aTag = new PostTag().setATag("cTag").setPostID(3L);
        postTagsMapper.insert(new PostTag().setPostID(1L).setATag("aTag"));
        postTagsMapper.insert(new PostTag().setPostID(2L).setATag("bTag"));
        postTagsMapper.insert(aTag);
        postTagsMapper.delete(aTag);
        assertEquals(postTagsMapper.getAll().size(), before + 2);
    }

    @Test
    public void shouldGetAllPostTags() {
        List<PostTag> before = postTagsMapper.getAll();
        PostTag newPostTag = new PostTag().setPostID(1L).setATag("aTag");
        before.add(newPostTag);
        postTagsMapper.insert(newPostTag);
        assertEquals(postTagsMapper.getAll().size(), before.size());
    }

    @Test
    public void shouldGetPostTagsByTag() {
        postTagsMapper.insert(new PostTag().setPostID(1L).setATag("aTag"));
        postTagsMapper.insert(new PostTag().setPostID(2L).setATag("aTag"));
        assertEquals(postTagsMapper.getPostTagsByATag("aTag").size(), 2);
    }

    @Test
    public void shouldGetPostTagsByPostID() {
        postTagsMapper.insert(new PostTag().setPostID(4L).setATag("oneTag"));
        postTagsMapper.insert(new PostTag().setPostID(4L).setATag("twoTag"));
        assertEquals(postTagsMapper.getPostTagsByPostID(4L).size(), 2);
    }





}
