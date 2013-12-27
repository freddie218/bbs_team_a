package com.thoughtworks.bbs.model;

import java.util.Date;

public class Post {
    private Long postId;
    private Long parentId;
    private String authorName;
    private String title;
    private String content;
    private Date createTime;
    private Date modifyTime;
    private Long creatorId;
    private Long modifierId;
    private Long liked_time;
    private boolean top;

    public Post setPostId(long postId) {
        this.postId = postId;
        return this;
    }

    public Post setParentId(long parentId) {
        this.parentId = parentId;
        return this;
    }

    public Post setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public Post setContent(String content) {
        this.content = content;
        return this;
    }

    public Post setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Post setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public Post setCreatorId(long creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public Post setModifierId(long modifierId) {
        this.modifierId = modifierId;
        return this;
    }

    public  Post setLikeTime(long liked_time){
        this.liked_time = liked_time;
        return this;
    }

    public  Post setTop(boolean top){
        this.top = top;
        return this;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public Long getLiked_time(){
        return liked_time;
    }

    public boolean getTop(){
        return top;
    }
}
