package top.zymd3414.sm_springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zymd3414.sm_springboot.pojo.Friendships;

import java.util.List;

@Mapper
public interface FriendshipsMapper {
    // 判断是否为好友
    String isFriend(Integer userId, Integer friendId);
    // 获取好友数据
    List<Friendships> getFriends(Integer userId);
    // 获取好友数据（好友作为发起请求方）
    List<Friendships> getFriends2(Integer userId);
    // 增加未读消息数
    void addNotReadMessage(Integer requesterId, Integer addresseeId);
    // 重置未读消息数
    void resetNotReadMessage(Integer requesterId, Integer addresseeId);
}
