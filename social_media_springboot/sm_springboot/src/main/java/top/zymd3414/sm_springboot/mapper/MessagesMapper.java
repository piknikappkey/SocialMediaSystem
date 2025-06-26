package top.zymd3414.sm_springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zymd3414.sm_springboot.pojo.Messages;

import java.util.List;

@Mapper
public interface MessagesMapper {
    // 获取消息
    List<Messages> getMessages(Integer senderId, Integer receiverId, Integer start, Integer size);
    // 设置消息已读
    void updateIsRead(List<Messages> list, Integer senderId, Integer receiverId);
    // 添加消息
    void addMessages(Messages messages);
}
