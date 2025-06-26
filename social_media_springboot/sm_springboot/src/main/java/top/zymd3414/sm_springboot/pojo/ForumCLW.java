package top.zymd3414.sm_springboot.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// 喜欢、收藏、历史类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumCLW {
    private Integer forumId;
    private Integer userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
}
