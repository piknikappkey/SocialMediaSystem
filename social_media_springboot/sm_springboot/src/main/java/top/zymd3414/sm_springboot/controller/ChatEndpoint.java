package top.zymd3414.sm_springboot.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zymd3414.sm_springboot.config.GetHttpSessionConfig;
import top.zymd3414.sm_springboot.mapper.FriendshipsMapper;
import top.zymd3414.sm_springboot.mapper.MessagesMapper;
import top.zymd3414.sm_springboot.mapper.UserMapper;
import top.zymd3414.sm_springboot.pojo.*;

import java.io.IOException;
// 线程安全的map集合
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/chat/{userId}", configurator = GetHttpSessionConfig.class)
@Component
public class ChatEndpoint {
    private static final ConcurrentHashMap<Integer, Session> onLineUsers = new ConcurrentHashMap<>();
    private HttpSession httpSession;
    private static UserMapper userMapper;
    private static FriendshipsMapper friendshipsMapper;
    private static MessagesMapper messagesMapper;
    private static ChatMessageUtils chatMessageUtils;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        ChatEndpoint.userMapper = userMapper; // 静态字段赋值
    }
    @Autowired
    public void setUserMapper(FriendshipsMapper friendshipsMapper) {
        ChatEndpoint.friendshipsMapper = friendshipsMapper; // 静态字段赋值
    }
    @Autowired
    public void setUserMapper(MessagesMapper messagesMapper) {
        ChatEndpoint.messagesMapper = messagesMapper; // 静态字段赋值
    }
    @Autowired
    public void setChatMessageUtils(ChatMessageUtils chatMessageUtils) {
        ChatEndpoint.chatMessageUtils = chatMessageUtils;
    }

    @OnOpen
    public void onLine(Session session, EndpointConfig config, @PathParam("userId") String id) {
        System.out.println(id);
        Integer userId = Integer.parseInt(id);
        // 获取用户自己的数据
        userId = UserIdEncryption.decode(userId);
        User user = userMapper.getUserData(userId);
        // 将session进行保存
//        httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        onLineUsers.put(userId, session);
        // 查找所有好友数据
        List<Friendships> friendships = friendshipsMapper.getFriends2(userId);
        // 将上线消息发送给所有好友
        broadcastFriend(friendships, "online", user);
        System.out.println(userId + "上线");
        // 获取已经上线的好友的信息
        friendships = friendshipsMapper.getFriends(userId);
        List<Friendships> onlineFriends = getOnlineFriends(friendships);
        // 发送给用户已经上线的好友的信息
        sendMessage(chatMessageUtils.getJson("onlineFriends", onlineFriends), session);
    }

    @OnMessage
    public void onMessage(String msg) {
        System.out.println(msg);
        // 接收消息对象
        Messages messages = (Messages) chatMessageUtils.getObject(msg, Messages.class);
        messages.setCreatedTime(LocalDateTime.now());
        // 要发送给在线好友的消息对象
        Messages sendMessages = new Messages(messages);
        // 保存消息数据
        messages.setSenderId(UserIdEncryption.decode(messages.getSenderId())); // 用户id解码
        messages.setReceiverId(UserIdEncryption.decode(messages.getReceiverId()));
        messagesMapper.addMessages(messages);
        // 设置好友的未读消息数
        friendshipsMapper.addNotReadMessage(messages.getReceiverId(), messages.getSenderId());
        // 获取消息接收方的session对象
        Session ses = onLineUsers.get(messages.getReceiverId());
        // 尝试发送消息给接收方
        if (ses == null) return;
        System.out.println("发送消息" + sendMessages + "给好友" + sendMessages.getReceiverId());
        sendMessage(chatMessageUtils.getJson("message", sendMessages), ses);
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String id) {
        Integer userId = Integer.parseInt(id);
        // 获取用户自己的数据
        userId = UserIdEncryption.decode(userId);
        User user = userMapper.getUserData(userId);
        user.setUserId(UserIdEncryption.encrypt(userId));
        System.out.println(userId + "下线");
        //将下线消息通知给所有好友
        // 查找所有好友数据
        List<Friendships> friendships = friendshipsMapper.getFriends2(userId);
        // 删除自己的在线信息
        onLineUsers.remove(userId);
        // 将消息发送给所有好友
        broadcastFriend(friendships, "outline", user);
    }

    // 发送消息
    private void sendMessage(String msg, Session session) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 用于将上线或下线消息发送给所有好友
    private void broadcastFriend(List<Friendships> friendships, String type, User user) {
        user.setUserId(UserIdEncryption.encrypt(user.getUserId())); // id加密
        for (Friendships friendship : friendships) {
            Session ses;
            // 查找在线好友，并尝试发起上线通知
            if ((ses = onLineUsers.get(friendship.getRequesterId()))!= null) {
                // 给好友信息的中附带用户信息
                friendship.setAddresseeUser(user);
                friendship.setRequesterId(UserIdEncryption.encrypt(friendship.getRequesterId())); // 加密
                friendship.setAddresseeId(UserIdEncryption.encrypt(friendship.getAddresseeId())); // 加密
                // 发送消息
                System.out.println("将" + friendship.getAddresseeId() + type + "消息发送给好友" + friendship.getRequesterId());
                String message = chatMessageUtils.getJson(type, friendship);
                sendMessage(message, ses);
            }
        }
    }

    // 获取已经上线的好友的信息
    private List<Friendships> getOnlineFriends(List<Friendships> friendships) {
        List<Friendships> onlineFriends = new ArrayList<>();
        for (Friendships friendship : friendships) {
            // 查找在线好友
            if (onLineUsers.containsKey(friendship.getAddresseeId())) {
                User addresseeUser = userMapper.getUserData(friendship.getAddresseeId());
                addresseeUser.setUserId(UserIdEncryption.encrypt(addresseeUser.getUserId())); // 加密
                friendship.setAddresseeUser(addresseeUser);
                friendship.setRequesterId(UserIdEncryption.encrypt(friendship.getRequesterId())); // 加密
                friendship.setAddresseeId(UserIdEncryption.encrypt(friendship.getAddresseeId())); // 加密
                // 添加在线好友
                onlineFriends.add(friendship);
            }
        }
        return onlineFriends;
    }
}
