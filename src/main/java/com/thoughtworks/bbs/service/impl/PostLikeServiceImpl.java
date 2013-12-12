package com.thoughtworks.bbs.service.impl;


import com.thoughtworks.bbs.mappers.PostLikeMapper;
import com.thoughtworks.bbs.model.PostLike;
import com.thoughtworks.bbs.service.PostLikeService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.LinkedList;
import java.util.List;

public class PostLikeServiceImpl implements PostLikeService{
    private SqlSessionFactory factory;

    public PostLikeServiceImpl(SqlSessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<PostLike> getPostLikeByUserID(Long userID) {
        SqlSession session = factory.openSession();
        List<PostLike> postLikes = new LinkedList<PostLike>();

        try
        {
            PostLikeMapper postLikeMapper = session.getMapper(PostLikeMapper.class);
            postLikes = postLikeMapper.getPostLikeByUserID(userID);
        } finally
        {
            session.close();
        }

        return postLikes;
    }

    @Override
    public List<PostLike> getPostLikeByPostID(Long postID) {
        SqlSession session = factory.openSession();
        List<PostLike> postLikes = new LinkedList<PostLike>();

        try
        {
            PostLikeMapper postLikeMapper = session.getMapper(PostLikeMapper.class);
            postLikes = postLikeMapper.getPostLikeByPostID(postID);
        } finally
        {
            session.close();
        }

        return postLikes;
    }

    @Override
    public void save(PostLike alike) {
        SqlSession session = factory.openSession();

        try
        {
            PostLikeMapper postLikeMapper = session.getMapper(PostLikeMapper.class);
            postLikeMapper.insert(alike);
            session.commit();
        } finally
        {
            session.close();
        }

    }

    @Override
    public Boolean isLiked(Long userID, Long postID) {
        List<PostLike> likedListByUserID = getPostLikeByUserID(userID);
        for(PostLike aLike : likedListByUserID)
        {
            if(postID == aLike.getPost_id())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deletePostLike(PostLike alike) {
        SqlSession session = factory.openSession();
        try
        {
            PostLikeMapper postLikeMapper = session.getMapper(PostLikeMapper.class);
            postLikeMapper.delete(alike);
            session.commit();
        } finally
        {
            session.close();
        }
    }
}
