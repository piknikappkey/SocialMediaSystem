package top.zymd3414.sm_springboot.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zymd3414.sm_springboot.pojo.BodyUser;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.pojo.User;
import top.zymd3414.sm_springboot.service.CodeService;
import top.zymd3414.sm_springboot.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CodeService codeService;

    // 获取用户数据(根据用户id返回用户信息)
    @GetMapping("/users")
    public Result getUsers(@RequestParam Integer userId) {
        return userService.getUserData(userId);
    }

    // 登录请求
    @PostMapping("/send_log")
    public Result log(@RequestBody BodyUser bodyUser){
        return switch (bodyUser.getLogMode()) {
            case 0 -> userService.log(bodyUser.getEmail(), bodyUser.getPassword(), bodyUser.getPassword2(), bodyUser.getCode()); // 登录
            case 1 -> userService.register(bodyUser.getEmail(), bodyUser.getPassword(), bodyUser.getPassword2(), bodyUser.getCode()); // 注册
            case 2 -> userService.forgetLog(bodyUser.getEmail(), bodyUser.getPassword(), bodyUser.getPassword2(), bodyUser.getCode()); // 忘记密码
            default -> Result.error("错误的登录请求！");
        };
    }

    // 获取验证码请求
    @GetMapping("/send_code")
    public Result sendCode(@RequestParam String email){
        return codeService.sendCode(email); // 获取验证码
    }
}
