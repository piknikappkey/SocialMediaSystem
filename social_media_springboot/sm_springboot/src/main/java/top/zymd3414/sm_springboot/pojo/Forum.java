package top.zymd3414.sm_springboot.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 论坛类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forum {
    private Integer forumId;
    private Integer userId;
    private String forumTitle;
    private String forumText;
    private String forumFeature;
    private String forumContentUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
}
