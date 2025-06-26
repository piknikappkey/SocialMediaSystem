package top.zymd3414.sm_springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zymd3414.sm_springboot.pojo.ForumComment;

import java.util.List;

@Mapper
public interface ForumCommentMapper {
    // 获取单个评论数据
    void setCommentHaveChild(Integer commentId);
    // 获取评论数据
    List<ForumComment> getComments(Integer forumId, Integer start, Integer size);
    // 获取子评论数据
    List<ForumComment> getChildComments(Integer forumId, Integer start, Integer size, Integer fatherCommentId);
    // 添加评论数据
    void addComment(ForumComment forumComment);
}
