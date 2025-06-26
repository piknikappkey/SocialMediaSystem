package top.zymd3414.sm_springboot.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import top.zymd3414.sm_springboot.mapper.CookieMapper;
import top.zymd3414.sm_springboot.mapper.UserMapper;
import top.zymd3414.sm_springboot.service.CookieService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class CookieServiceImpl implements CookieService {
    @Autowired // 数据库操作
    private UserMapper userMapper;
    @Autowired
    private CookieMapper cookieMapper;

    // 写入cookie
    public void writeCookie(String email, String cookie) {
        Integer id = userMapper.getUserId(email); // 获取id
        Integer sqlId = cookieMapper.getUserId(cookie);
        if (sqlId == null) { // cookie表中不存在
            try {
                cookieMapper.addCookie(id, cookie, LocalDateTime.now());
            }catch (Exception e){ // 如果发生错误（id已存在）
                cookieMapper.delById(id);
                cookieMapper.addCookie(id, cookie, LocalDateTime.now());
            }
            return;
        }
        if (!Objects.equals(id, sqlId)) { // cookie表已存在但id不相等
            cookieMapper.delByCookie(cookie);
            cookieMapper.addCookie(id, cookie, LocalDateTime.now());
            return;
        }
        if (id.equals(sqlId)) { // 存在且相等
            cookieMapper.update(cookie, LocalDateTime.now());
            return;
        }
    }

    // 设置cookie
    @Override
    public void setCookie(HttpServletResponse response, String userCookie) {
        if(Objects.equals(userCookie, "None")) userCookie = UUID.randomUUID().toString();
        // 设置cookie
        ResponseCookie cookie = ResponseCookie.from("userCookie", userCookie)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // SameSite值
                .path("/")
                .maxAge(3600 * 24 * 3)
                .build();
        // 将Cookie添加到HTTP响应头
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
