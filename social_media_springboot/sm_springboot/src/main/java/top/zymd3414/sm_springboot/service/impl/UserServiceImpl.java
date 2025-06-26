package top.zymd3414.sm_springboot.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zymd3414.sm_springboot.mapper.UserMapper;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.pojo.User;
import top.zymd3414.sm_springboot.pojo.UserIdEncryption;
import top.zymd3414.sm_springboot.service.CodeService;
import top.zymd3414.sm_springboot.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired // 数据库操作
    private UserMapper userMapper;
    @Autowired // code逻辑操作
    private CodeService codeService;

    @Override // 获取用户数据
    public Result getUserData(Integer userId) {
        if(userId == null || userId == 0) return Result.error("参数错误！"); // 如果查找不到用户则返回无
        userId = UserIdEncryption.decode(userId);
        User user = userMapper.getUserData(userId); // 查找到的用户
        user.setPassword("none"); // 删除密码等敏感信息
        // 对用户id加密
        user.setUserId(UserIdEncryption.encrypt(user.getUserId()));
        return Result.success(user);
    }

    @Override // 通过邮箱获取用户数据
    public Result getUserData(String email) {
        Integer userId = userMapper.getUserId(email);
        if (userId == null) return Result.error("用户不存在");
        User user = userMapper.getUserData(userId);
        user.setPassword("none");
        user.setUserId(UserIdEncryption.encrypt(userId));
        return Result.success(user);
    }

    @Override // 登录操作
    public Result log(String email, String password, String password2, String code) {
        String ps = userMapper.getUserPassword(email); // 查找对应密码
        if(password == null) return Result.error("请输入密码"); // 无密码返回
        if (!ps.equals(password)) return Result.error("密码不正确");// 密码不正确返回
        return getUserData(email);
    }

    @Override // 注册操作
    public Result register(String email, String password, String password2, String code) {
        // 检查验证码
        String codeStr = codeService.verCode(email, code);
        if (codeStr != null) { // 验证失败
            return Result.error(codeStr); // 返回错误信息
        }
        // 验证码正确
        // 查找id
        Integer userId = userMapper.getUserId(email);
        if (userId == null) { // 不存在
            User user = new User(email.split("@")[0], email, password);
            userMapper.addUser(user);
        } else { // 已存在则更新密码等信息
            userMapper.updatePassword(userId, password);
        }
        return getUserData(email);
    }

    @Override // 忘记密码登录操作
    public Result forgetLog(String email, String password, String password2, String code) {
        // 检查验证码
        String codeStr = codeService.verCode(email, code);
        if (codeStr != null) { // 验证失败
            return Result.error(codeStr); // 返回错误信息
        }
        // 查找id
        Integer userId = userMapper.getUserId(email);
        if (userId == null) { // 不存在
            User user = new User(email.split("@")[0], email, password);
            userMapper.addUser(user);
        } else { // 已存在则更新密码
            userMapper.updatePassword(userId, password);
        }
        return getUserData(email);
    }
}
