package top.zymd3414.sm_springboot.service;

import top.zymd3414.sm_springboot.pojo.Result;

public interface FriendshipsService {
    Result friendSearch(String searchValue, Integer start, Integer size, Integer userId);

    Result getFriend(Integer userId);

    Result setNotReadMessages(Integer requesterId, Integer addresseeId);
}
