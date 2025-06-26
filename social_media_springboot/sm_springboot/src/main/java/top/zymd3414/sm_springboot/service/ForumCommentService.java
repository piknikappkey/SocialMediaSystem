package top.zymd3414.sm_springboot.service;

import top.zymd3414.sm_springboot.pojo.ForumComment;
import top.zymd3414.sm_springboot.pojo.Result;

public interface ForumCommentService {
    // 获取父评论
    Result getComments(Integer forumId, Integer start, Integer size, Integer userId);
    // 获取子评论
    Result getChildComments(Integer forumId, Integer start, Integer size, Integer fatherCommentId, Integer userId);
    // 添加评论
    Result addComment(ForumComment forumComment);

}
