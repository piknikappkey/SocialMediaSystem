package top.zymd3414.sm_springboot.service;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {
    // 写入cookie
    public void writeCookie(String email, String cookie);
    // 设置cookie
    public void setCookie(HttpServletResponse response, String userCookie);
}
