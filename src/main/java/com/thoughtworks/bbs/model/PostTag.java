package com.thoughtworks.bbs.model;

public class PostTag {
    private Long postID;
    private String aTag;


    public Long getPostID() {
        return postID;
    }

    public PostTag setPostID(Long postID) {
        this.postID = postID;
        return this;
    }

    public String getATag() {
        return aTag;
    }

    public PostTag setATag(String aTag) {
        this.aTag = aTag;
        return this;
    }
}
