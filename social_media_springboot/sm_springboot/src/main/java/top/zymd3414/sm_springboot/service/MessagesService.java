package top.zymd3414.sm_springboot.service;

import top.zymd3414.sm_springboot.pojo.Messages;
import top.zymd3414.sm_springboot.pojo.Result;

public interface MessagesService {
    Result getMessages(Integer senderId, Integer receiverId, Integer start, Integer size);

    Result addMessages(Messages messages);
}
