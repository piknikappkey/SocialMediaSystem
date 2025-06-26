package top.zymd3414.sm_springboot.service;
import top.zymd3414.sm_springboot.pojo.BodyUser;
import top.zymd3414.sm_springboot.pojo.Result;

public interface CodeService {
    // 请求验证码
    Result sendCode(String email);

    // 验证验证码
    String verCode(String email, String code);
}
