package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.PostLikeMapper;
import com.thoughtworks.bbs.mappers.PostTagsMapper;
import com.thoughtworks.bbs.model.PostLike;
import com.thoughtworks.bbs.model.PostTag;
import com.thoughtworks.bbs.service.PostTagService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.LinkedList;
import java.util.List;

public class PostTagServiceImpl implements PostTagService {
    private SqlSessionFactory factory;

    public PostTagServiceImpl(SqlSessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<PostTag> getPostTagsByATag(String tag) {
        SqlSession session = factory.openSession();
        List<PostTag> postTags = new LinkedList<PostTag>();

        try
        {
            PostTagsMapper postTagsMapper = session.getMapper(PostTagsMapper.class);
            postTags = postTagsMapper.getPostTagsByATag(tag);
        } finally
        {
            session.close();
        }

        return postTags;
    }

    @Override
    public List<PostTag> getPostTagByPostID(Long postID) {
        SqlSession sqlSession = factory.openSession();
        List<PostTag> postTags = new LinkedList<PostTag>();

        try
        {
            PostTagsMapper postTagsMapper = sqlSession.getMapper(PostTagsMapper.class);
            postTags = postTagsMapper.getPostTagsByPostID(postID);
        } finally {
            sqlSession.close();
        }
        return  postTags;
    }

    @Override
    public void save(PostTag postTag) {
        SqlSession sqlSession = factory.openSession();

        try
        {
            PostTagsMapper postTagsMapper = sqlSession.getMapper(PostTagsMapper.class);
            postTagsMapper.insert(postTag);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void deletePostTag(PostTag postTag) {
        SqlSession sqlSession = factory.openSession();

        try
        {
            PostTagsMapper postTagsMapper = sqlSession.getMapper(PostTagsMapper.class);
            postTagsMapper.delete(postTag);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }

    }
}
