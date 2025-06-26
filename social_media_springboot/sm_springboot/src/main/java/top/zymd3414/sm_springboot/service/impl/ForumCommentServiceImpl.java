package top.zymd3414.sm_springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zymd3414.sm_springboot.mapper.ForumCommentMapper;
import top.zymd3414.sm_springboot.mapper.UserMapper;
import top.zymd3414.sm_springboot.pojo.ForumComment;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.pojo.UserIdEncryption;
import top.zymd3414.sm_springboot.service.ForumCommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumCommentServiceImpl implements ForumCommentService {
    @Autowired
    private ForumCommentMapper forumCommentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result getComments(Integer forumId, Integer start, Integer size, Integer userId) {
        // 如果查找不到用户
//        if (userId == 0) {
            List<ForumComment> comments = forumCommentMapper.getComments(forumId, start, size);
            for (ForumComment comment : comments) {
                comment.setUserName(userMapper.getUserName(comment.getUserId()));
                comment.setAvatarUrl(userMapper.getAvatarUrl(comment.getUserId()));
                Integer toUserId = comment.getToUserId();
                if(toUserId != null) {
                    comment.setToUserName(userMapper.getUserName(toUserId));
                }
            }
            // 返回大众化的评论
            return Result.success(comments);
//        };
//        userId = UserIdEncryption.decode(userId);
//        return Result.success(forumCommentMapper.getComments(start, size));
    }

    @Override
    public Result getChildComments(Integer forumId, Integer start, Integer size, Integer fatherCommentId, Integer userId) {
        // 如果查找不到用户
//        if (userId == 0) {
            // 返回大众化的评论
            List<ForumComment> comments = forumCommentMapper.getChildComments(forumId, start, size, fatherCommentId);
            for (ForumComment comment : comments) {
                comment.setUserName(userMapper.getUserName(comment.getUserId()));
                comment.setAvatarUrl(userMapper.getAvatarUrl(comment.getUserId()));
                Integer toUserId = comment.getToUserId();
                if(toUserId != null) {
                    comment.setToUserName(userMapper.getUserName(toUserId));
                }
            }
            return Result.success(comments);
//        };
//        userId = UserIdEncryption.decode(userId);
//        return Result.success(forumCommentMapper.getChildComments(forumId, start, size, fatherCommentId));
    }

    @Override
    public Result addComment(ForumComment forumComment) {
        try {
            // 如果查找不到用户
            if (forumComment.getUserId() == 0) {
                // 返回大众化的论坛
                return Result.error("用户不存在");
            };
            forumComment.setUserId(UserIdEncryption.decode(forumComment.getUserId()));
            forumComment.setLikeNum(0);
            forumComment.setHaveChild(0);
            forumComment.setCreatedTime(LocalDateTime.now());
            System.out.println(forumComment);
            if(forumComment.getFatherId() != null) {
                forumCommentMapper.setCommentHaveChild(forumComment.getFatherId());
            }
            forumCommentMapper.addComment(forumComment);
            return Result.success();
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
