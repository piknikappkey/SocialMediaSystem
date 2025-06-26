package top.zymd3414.sm_springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zymd3414.sm_springboot.pojo.Messages;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.service.FriendshipsService;
import top.zymd3414.sm_springboot.service.MessagesService;

@RestController
public class FriendController {
    @Autowired
    private FriendshipsService friendshipsService;
    @Autowired
    private MessagesService messagesService;
    // 获取好友列表
    @GetMapping("/friends")
    public Result getFriends(@RequestParam Integer userId) {
        return friendshipsService.getFriend(userId);
    }
    // 搜索用户
    @GetMapping("/friendSearches")
    public Result friendSearch(@RequestParam String searchValue,
                               @RequestParam Integer userId,
                               @RequestParam Integer start,
                               @RequestParam Integer size) {
        return friendshipsService.friendSearch(searchValue, start, size, userId);
    }
    // 获取与好友的历史消息
    @GetMapping("/messages")
    public Result getMessages(@RequestParam Integer senderId,
                          @RequestParam Integer receiverId,
                          @RequestParam Integer start,
                          @RequestParam Integer size) {
        return messagesService.getMessages(senderId, receiverId, start, size);
    }
    // 添加消息
    @PostMapping("/messages")
    public Result postMessages(@RequestBody Messages messages) {
        return messagesService.addMessages(messages);
    }
    // 设置未读消息数
    @PostMapping("/notReadMessages")
    public Result setNotReadMessages(@RequestParam Integer requesterId, @RequestParam Integer addresseeId) {
        return friendshipsService.setNotReadMessages(requesterId, addresseeId);
    }
}
