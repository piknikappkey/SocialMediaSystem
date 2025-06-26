package top.zymd3414.sm_springboot.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 论坛计数类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumNumber {
    private Integer forumId;
    private Integer likeNum; // 喜欢
    private Integer collectNum; // 收藏
    private Integer watchNum; // 观看
    private Integer transpondNum; // 转发
    private Integer commentNum; // 评论
    public ForumNumber(Forum forum) {
        this.forumId = forum.getForumId();
        this.likeNum = 0;
        this.collectNum = 0;
        this.watchNum = 0;
        this.transpondNum = 0;
        this.commentNum = 0;
    }
}
