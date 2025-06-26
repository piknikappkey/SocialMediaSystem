package top.zymd3414.sm_springboot.service;
import top.zymd3414.sm_springboot.pojo.BodyUser;
import top.zymd3414.sm_springboot.pojo.Result;

public interface UserService {
    // 获取用户数据
    Result getUserData(Integer userId);

    Result getUserData(String email);

    // 登录
    Result log(String email, String password, String password2, String code);

    // 注册
    Result register(String email, String password, String password2, String code);

    // 忘记密码
    Result forgetLog(String email, String password, String password2, String code);
}
