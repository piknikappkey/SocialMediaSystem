package top.zymd3414.sm_springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zymd3414.sm_springboot.mapper.FriendshipsMapper;
import top.zymd3414.sm_springboot.mapper.UserMapper;
import top.zymd3414.sm_springboot.pojo.Friendships;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.pojo.User;
import top.zymd3414.sm_springboot.pojo.UserIdEncryption;
import top.zymd3414.sm_springboot.service.FriendshipsService;

import java.util.List;

@Service
public class FriendshipsServiceImpl implements FriendshipsService {
    @Autowired
    private FriendshipsMapper friendshipsMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Result friendSearch(String searchValue, Integer start, Integer size, Integer userId) {
        // 如果查找不到用户
        if (userId == 0) return Result.error("查找不到用户，请重新登录");
        userId = UserIdEncryption.decode(userId);
        List<User> userList = userMapper.searchUser('%' + searchValue + '%', start, size);
        for (User user : userList) {
            user.setPassword(null);
            user.setUserId(UserIdEncryption.encrypt(user.getUserId()));
        }
        return Result.success(userList);
    }

    @Override
    public Result getFriend(Integer userId) {
        // 如果查找不到用户
        if (userId == 0) return Result.error("查找不到用户，请重新登录");
        userId = UserIdEncryption.decode(userId);
        List<Friendships> friendships = friendshipsMapper.getFriends(userId);
        for (Friendships friendship : friendships) {
            User user = userMapper.getUserData(friendship.getAddresseeId());
            user.setPassword(null);
            //  用户id加密
            user.setUserId(UserIdEncryption.encrypt(user.getUserId()));
            friendship.setAddresseeUser(user);
            friendship.setRequesterId(UserIdEncryption.encrypt(friendship.getRequesterId()));
            friendship.setAddresseeId(UserIdEncryption.encrypt(friendship.getAddresseeId()));
        }
        return Result.success(friendships);
    }

    @Override
    public Result setNotReadMessages(Integer requesterId, Integer addresseeId) {
        requesterId = UserIdEncryption.decode(requesterId);
        addresseeId = UserIdEncryption.decode(addresseeId);
        friendshipsMapper.resetNotReadMessage(requesterId, addresseeId);
        return Result.success();
    }
}
