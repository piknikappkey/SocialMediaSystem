package top.zymd3414.sm_springboot.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// 用户关系类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendships {
    private Integer friendshipId;
    private Integer requesterId;
    private Integer addresseeId;
    private String status;
    private Integer notReadMessages;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
    private User addresseeUser;
}
