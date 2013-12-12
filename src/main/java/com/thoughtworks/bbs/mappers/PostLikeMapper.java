package com.thoughtworks.bbs.mappers;


import com.thoughtworks.bbs.model.PostLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PostLikeMapper {

    @Insert(
            "INSERT INTO likes_table(post_id, user_id) " +
                    "VALUES (#{postID}, #{userID})"
    )
    void insert(PostLike alike);

    @Select(
            "SELECT post_id as postID, user_id as userID " +
                    "FROM likes_table " +
                    "WHERE (user_id = #{userID})"
    )
    List<PostLike> getPostLikeByUserID(Long user_id);

    @Select(
            "SELECT post_id as postID, user_id as userID " +
                    "FROM likes_table " +
                    "WHERE (post_id = #{postID})"
    )
    List<PostLike> getPostLikeByPostID(Long post_id);

    @Select(
            "SELECT post_id as postID, user_id as userID " +
                    "FROM likes_table "
    )
    List<PostLike> getAll();

    @Delete(
            "DELETE FROM likes_table WHERE post_id=#{postID} and user_id=#{userID}"
    )
    void delete(PostLike aPostLike);
}
