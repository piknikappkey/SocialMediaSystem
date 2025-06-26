package top.zymd3414.sm_springboot.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 评论类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumComment {
    private Integer commentId;
    private Integer forumId;
    private Integer fatherId;
    private Integer haveChild;
    private Integer userId;
    private Integer toUserId;
    private Integer likeNum;
    private String comment;
    private String comImgUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    private String userName;
    private String avatarUrl;
    private String toUserName;
}
