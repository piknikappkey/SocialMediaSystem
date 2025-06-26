package top.zymd3414.sm_springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zymd3414.sm_springboot.mapper.FriendshipsMapper;
import top.zymd3414.sm_springboot.mapper.MessagesMapper;
import top.zymd3414.sm_springboot.pojo.Messages;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.pojo.UserIdEncryption;
import top.zymd3414.sm_springboot.service.MessagesService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagesServiceImpl implements MessagesService {
    @Autowired
    MessagesMapper messagesMapper;
    @Autowired
    FriendshipsMapper friendshipsMapper;
    // 获取历史消息
    @Override
    public Result getMessages(Integer senderId, Integer receiverId, Integer start, Integer size) {
        senderId = UserIdEncryption.decode(senderId);
        receiverId = UserIdEncryption.decode(receiverId);
        // 获取消息列表
        List<Messages> messages = messagesMapper.getMessages(senderId, receiverId, start, size);
        // 筛选未读消息
        List<Messages> unreadMessages = messages.stream()
                .filter(msg -> msg.getRead() == 0)
                .collect(Collectors.toList());
        // 更新数据库
        if (!unreadMessages.isEmpty()) {
            messagesMapper.updateIsRead(unreadMessages, senderId, receiverId);
            // 同步更新返回对象状态
            unreadMessages.forEach(msg -> msg.setRead(false));
        }
        // 设置好友的未读消息为0
        friendshipsMapper.resetNotReadMessage(senderId, receiverId);
        for (Messages message: messages) { // 用户id加密
            message.setSenderId(UserIdEncryption.encrypt(message.getSenderId()));
            message.setReceiverId(UserIdEncryption.encrypt(message.getReceiverId()));
        }
        return Result.success(messages);
    }
    // 添加消息
    @Override
    public Result addMessages(Messages messages) {
        messages.setSenderId(UserIdEncryption.decode(messages.getSenderId()));
        messages.setReceiverId(UserIdEncryption.decode(messages.getReceiverId()));
        messages.setCreatedTime(LocalDateTime.now());
        messagesMapper.addMessages(messages);
        return Result.success();
    }
}
