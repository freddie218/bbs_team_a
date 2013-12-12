package com.thoughtworks.bbs.service;


import com.thoughtworks.bbs.model.PostLike;

import java.util.List;

public interface PostLikeService {

    List<PostLike> getPostLikeByUserID(Long userID);

    List<PostLike> getPostLikeByPostID(Long postID);

    void save(PostLike alike);

    Boolean isLiked(Long userID, Long postID);

    void deletePostLike(PostLike alike);
}
