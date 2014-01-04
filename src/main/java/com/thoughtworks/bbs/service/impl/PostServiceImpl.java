package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.PostMapper;
import com.thoughtworks.bbs.model.Post;
import com.thoughtworks.bbs.model.PostValidator;
import com.thoughtworks.bbs.service.PostService;
import com.thoughtworks.bbs.service.ServiceResult;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PostServiceImpl implements PostService {
    private SqlSessionFactory factory;

    public PostServiceImpl(SqlSessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public Post get(Long postId) {
        SqlSession session = factory.openSession();
        Post post = null;
        try {
            PostMapper postMapper = session.getMapper(PostMapper.class);
            post = postMapper.get(postId);
        } finally {
            session.close();
        }

        return post;
    }

    @Override
    public List<Post> findMainPostByAuthorName(String authorName) {
        SqlSession session = factory.openSession();
        List<Post> posts = new LinkedList<Post>();

        try {
            PostMapper postMapper = session.getMapper(PostMapper.class);
            posts = postMapper.findMainPostByAuthorName(authorName);
        } finally {
            session.close();
        }

        return posts;
    }

    @Override
    public ServiceResult<Post> save(Post post) {
        Map<String,String> errors = new PostValidator().validate(post);

        SqlSession session = factory.openSession();
        if (errors.isEmpty()) {
            try {
                PostMapper postMapper = session.getMapper(PostMapper.class);
                if (null == post.getPostId()) {
                    postMapper.insert(post);
                } else {
                    postMapper.update(post);
                }

                session.commit();
            } finally {
                session.close();
            }
        }

        return new ServiceResult<Post>(errors, post);
    }

    @Override
    public void delete(Post post) {
        SqlSession session = factory.openSession();

        try {
            PostMapper postMapper = session.getMapper(PostMapper.class);
            if(post.getParentId().equals(0L)) {
                List<Post> subPosts = findAllPostByMainPost(post.getPostId());
                for (Post subPost : subPosts) {
                    postMapper.delete(subPost);
                }
            }
            postMapper.delete(post);
            session.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Post> findAllPostByMainPost(Long postId) {
        SqlSession session = factory.openSession();
        List<Post> posts = new LinkedList<Post>();

        try {
            PostMapper postMapper = session.getMapper(PostMapper.class);

            posts = postMapper.findAllPostByMainPost(postId);
        } finally {
            session.close();
        }

        return posts;
    }

    @Override
    public List<Post> findAllPostsOrderByTime() {
        SqlSession session = factory.openSession();
        List<Post> posts = new LinkedList<Post>();

        try {
            PostMapper postMapper = session.getMapper(PostMapper.class);
            posts = postMapper.findAllTopmostPostsOrderByTime();
            posts.addAll(postMapper.findAllNormalPostsOrderByTime());
        } finally {
            session.close();
        }

        return posts;
    }

    @Override
    public List<Post> findMainPostByAuthorNameSortedByCreateTime(String authorName) {
        List<Post> postByAuthorName = findMainPostByAuthorName(authorName);

        Collections.sort(postByAuthorName, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return post2.getCreateTime().compareTo(post1.getCreateTime());
            }
        });
        return postByAuthorName;
    }

    @Override
    public Long getPostIdByAuthorAndCreateTime(String name, Date time) {

        SqlSession session = factory.openSession();
        try
        {
            PostMapper postMapper = session.getMapper(PostMapper.class);
            return postMapper.getPostIDByNameAndTime(name, time);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Post> searchPost(String author, String title, String content, String start, String end) {
        if(author == null) author = "";
        if(title == null) title = "";
        if(content == null) content = "";
        if(start == null||start == "" ) start = "1949-10-1";
        if(end == null || end == "") end = "9999-12-30";
        author = addFilter(author);
        title = addFilter(title);
        content = addFilter(content);
        end = addOneDay(end);
        SqlSession session = factory.openSession();
        PostMapper postMapper = session.getMapper(PostMapper.class);
        List<Post> posts = postMapper.searchPost(author, title, content, start, end);
        session.close();
        return posts;
    }

    private String addFilter(String param){
        if(param == null) param = "";
        return "%"+param+"%";
    }

    private String addOneDay(String dateStr){
        DateFormat dm = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dm.parse(dateStr);
            Calendar c =Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            date = c.getTime();
            dateStr = dm.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    @Override
    public void setTopMostPost(String postID) {
        SqlSession session = factory.openSession();
        try {
            PostMapper postMapper = session.getMapper(PostMapper.class);
            Post post = postMapper.get(Long.parseLong(postID));
            post.setTop(true);
            postMapper.update(post);
            session.commit();
        } finally {
            session.close();
        }
    }
}
