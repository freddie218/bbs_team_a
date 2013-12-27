package com.thoughtworks.bbs.mappers;

import com.thoughtworks.bbs.model.Post;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;


public interface PostMapper {

    @Insert(
        "INSERT INTO post(parent_id, author_name, title, content, create_time, modify_time, creator_id, modifier_id, liked_time, top) " +
        "VALUES (#{parentId}, #{authorName}, #{title}, #{content}, #{createTime}, #{modifyTime}, #{creatorId}, #{modifierId}, #{liked_time}, #{top})"
    )
    void insert(Post item);

    @Select(
            "SELECT id as postId, parent_id as parentId, author_name as authorName, title, content, create_time as createTime, " +
                    "modify_time as modifyTime, creator_id as creatorId, modifier_id as modifierId, liked_time as liked_time, top as top " +
                    "FROM post " +
                    "WHERE id = #{postId}"
    )
    Post get(Long postId);

    @Select(
            "SELECT id as postId, parent_id as parentId, author_name as authorName, title, content, create_time as createTime, " +
                    "modify_time as modifyTime, creator_id as creatorId, modifier_id as modifierId, liked_time as liked_time, top as top " +
                    "FROM post "
    )
    List<Post> getAll();

    @Select(
            "SELECT id as postId, parent_id as parentId, author_name as authorName, title, content, create_time as createTime, " +
                    "modify_time as modifyTime, creator_id as creatorId, modifier_id as modifierId, liked_time as liked_time, top as top " +
                    "FROM post " +
                    "WHERE author_name = #{authorName} and parent_id = 0"
    )
    List<Post> findMainPostByAuthorName(String authorName);

    @Update(
        "UPDATE post " +
        "SET parent_id=#{parentId}, author_name=#{authorName}, title=#{title}, content=#{content}, create_time=#{createTime}," +
                " modify_time=#{modifyTime}, creator_id=#{creatorId}, modifier_id=#{modifierId}, liked_time=#{liked_time}, top=#{top} " +
        "WHERE id=#{postId}"
    )
    void update(Post post);

    @Delete(
        "DELETE FROM post WHERE id=#{postId}"
    )
    void delete(Post post);

    @Delete(
            "DELETE FROM post "
    )
    void deleteAll();

    @Select(
            "SELECT id as postId, parent_id as parentId, author_name as authorName, title, content, create_time as createTime, " +
                    "modify_time as modifyTime, creator_id as creatorId, modifier_id as modifierId, liked_time as liked_time, top as top " +
                    "FROM post " +
                    "WHERE (id = #{postId} and parent_id = 0)" +
                    "OR parent_id = #{postId} " +
                    "ORDER BY id asc"
    )
    List<Post> findAllPostByMainPost(Long postId);

    @Select(
            "SELECT id as postId, parent_id as parentId, author_name as authorName, title, content, create_time as createTime, " +
                    "modify_time as modifyTime, creator_id as creatorId, modifier_id as modifierId, liked_time as liked_time, top as top " +
                    "FROM post " +
                    "WHERE parent_id = 0 AND top = false " +
                    "ORDER BY create_time desc"
    )
    List<Post> findAllNormalPostsOrderByTime();

    @Select(
            "SELECT id as postId FROM post WHERE (author_name = #{name} and create_time = #{time})"

    )
    Long getPostIDByNameAndTime(@Param(value="name")String name, @Param(value="time") Date time);

     @Select(
             "SELECT id as postId, parent_id as parentId, author_name as authorName, title, content, create_time as createTime, " +
                    "modify_time as modifyTime, creator_id as creatorId, modifier_id as modifierId, liked_time as liked_time, top as top  " +
                    "FROM post " +
                    "WHERE (parent_id = 0 and author_name like #{author} and title like #{title} and content like #{content} and create_time >= #{starttime} and create_time < #{endtime}) " +
                    "ORDER BY create_time DESC"
    )
    List<Post> searchPost(@Param(value="author") String author, @Param(value="title") String title, @Param(value="content") String content, @Param(value="starttime")String startTime, @Param(value="endtime")String endTime);


    @Select(
            "SELECT id as postId, parent_id as parentId, author_name as authorName, title, content, create_time as createTime, " +
                    "modify_time as modifyTime, creator_id as creatorId, modifier_id as modifierId, liked_time as liked_time, top as top " +
                    "FROM post " +
                    "WHERE parent_id = 0 AND top = true " +
                    "ORDER BY create_time desc"
    )
    List<Post> findAllTopmostPostsOrderByTime();
}
