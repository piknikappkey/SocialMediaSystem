package top.zymd3414.sm_springboot.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 用户消息类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Messages {
    private Integer messageId;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private String messageType;
    private boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedTime;

    public Messages(Messages messages) {
        this.messageId = messages.getMessageId();
        this.senderId = messages.getSenderId();
        this.receiverId = messages.getReceiverId();
        this.content = messages.getContent();
        this.messageType = messages.getMessageType();
        this.isRead = false;
        this.createdTime = messages.getCreatedTime();
        this.deletedTime = messages.getDeletedTime();
    }

    public int getRead() {
        return isRead ? 1 : 0;
    }
}
