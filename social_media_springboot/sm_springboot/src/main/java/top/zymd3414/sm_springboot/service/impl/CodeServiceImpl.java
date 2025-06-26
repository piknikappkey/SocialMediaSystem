package top.zymd3414.sm_springboot.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import top.zymd3414.sm_springboot.mapper.CodeMapper;
import top.zymd3414.sm_springboot.pojo.BodyUser;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.pojo.UserCode;
import top.zymd3414.sm_springboot.service.CodeService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private CodeMapper codeMapper;

    // 发送验证码
    @Override
    public Result sendCode(String email) {
        // 随机数
        String randCode = randomCode();
        // 查找数据库
        UserCode userCode = codeMapper.getUserCode(email);
        if(userCode == null){ // 找不到则写入数据库
            codeMapper.addCode(email, randCode, LocalDateTime.now());
            send(email, randCode); // 发送
            return Result.success("已发送验证码");
        }
        // 找到则检查验证码时间，时间过短返回错误
        if(Duration.between(userCode.getCreatedTime(), LocalDateTime.now()).toMinutes() < 5){
            return Result.error("发送验证码过于频繁！");
        }
        // 更新
        codeMapper.updateCode(email, randCode, LocalDateTime.now());
        send(email, randCode); // 发送
        return Result.success("已发送验证码");
    }
    // 验证验证码
    @Override
    public String verCode(String email, String code) {
        // 获取验证码
        UserCode userCode = codeMapper.getUserCode(email);
        // 检查
        if (userCode == null) { // 验证码表中不存在
            return "请先获取验证码！";
        }
        if (!Objects.equals(userCode.getCode(), code)) { // 验证码不正确
            return "输入的验证码不正确！";
        }
        return null;
    }
    // 创建随机验证码
    private static String randomCode() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 生成100000到999999之间的随机数
        return String.valueOf(number);
    }
    // 发送验证码
    private void send(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2803796204@qq.com"); // 发件人邮箱
        message.setTo(email);
        message.setSubject("验证码通知");
        message.setText("您的验证码为：" + code + "，5分钟内有效。");
        mailSender.send(message);
    }
}
