package top.zymd3414.sm_springboot.mapper;
import org.apache.ibatis.annotations.Mapper;
import top.zymd3414.sm_springboot.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {
    // 获取用户数据
    User getUserData(Integer userId);

    // 获取用户名
    String getUserName(Integer userId);

    // 获取用户头像地址
    String getAvatarUrl(Integer userId);

    // 通过email获取用户密码
    String getUserPassword(String email);

    // 通过email获取用户id
    Integer getUserId(String email);

    // 添加用户数据
    void addUser(User user);

    // 更新用户密码
    void updatePassword(Integer userId, String password);

    // 搜索用户
    List<User> searchUser(String searchValue, Integer start, Integer size);
}
