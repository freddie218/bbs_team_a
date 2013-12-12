package com.thoughtworks.bbs.model;

public class PostLike {
    private Long postID;
    private Long userID;

    public PostLike setPostID(Long post_id)
    {
        this.postID = post_id;
        return this;
    }

    public  PostLike setUserID(Long user_id)
    {
        this.userID = user_id;
        return this;
    }

    public Long getPost_id()
    {
        return postID;
    }

    public Long getUser_id()
    {
        return userID;
    }
}
