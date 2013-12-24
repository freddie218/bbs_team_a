package com.thoughtworks.bbs.service;

import com.thoughtworks.bbs.model.PostLike;
import com.thoughtworks.bbs.model.PostTag;

import java.util.Date;
import java.util.List;

public interface PostTagService {

    List<PostTag> getPostTagsByATag(String tag);

    List<PostTag> getPostTagByPostID(Long postID);

    void save(PostTag postTag);

    void deletePostTag(PostTag postTag);

}
