package top.zymd3414.sm_springboot.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageUtils {
    @Autowired
    private ObjectMapper objectMapper;

    public String getJson(String type, Object message) {
        ChatResult chatResult = new ChatResult(type, message);
        try {
            return objectMapper.writeValueAsString(chatResult);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Object getObject(String message, Class clazz) {
        try {
            return objectMapper.readValue(message, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
