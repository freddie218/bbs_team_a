package com.thoughtworks.bbs.mappers;

import com.thoughtworks.bbs.model.PostTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface PostTagsMapper {
    @Insert(
            "INSERT INTO tags_table(post_id, tag) " +
                    "VALUES (#{postID}, #{aTag})"
    )
    void insert(PostTag aTag);

    @Select(
            "SELECT post_id as postID, tag as aTag " +
                    "FROM tags_table " +
                    "WHERE (tag = #{aTag})"
    )
    List<PostTag> getPostTagsByATag(String tag);

    @Select(
            "SELECT post_id as postID, tag as aTag " +
                    "FROM tags_table " +
                    "WHERE (post_id = #{postID})"
    )
    List<PostTag> getPostTagsByPostID(Long post_id);

    @Select(
            "SELECT post_id as postID, tag as aTag " +
                    "FROM tags_table "
    )
    List<PostTag> getAll();

    @Delete(
            "DELETE FROM tags_table WHERE post_id=#{postID} and tag=#{aTag}"
    )
    void delete(PostTag aPostTag);
}
