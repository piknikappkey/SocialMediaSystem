package top.zymd3414.sm_springboot.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatResult {
    private String type; // 消息类型
    private Object data; // 返回数据
    public ChatResult(String type, Object data) {
        this.type = type;
        this.data = data;
    }
}
