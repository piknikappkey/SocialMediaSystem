package top.zymd3414.sm_springboot.controller;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zymd3414.sm_springboot.pojo.UserIdEncryption;
import top.zymd3414.sm_springboot.service.CookieService;

@Slf4j
@RestController
public class CookieController {
    @Autowired
    private CookieService cookieService;

    // 设置网页cookie
    @GetMapping("/set_cookie")
    public void setCookie(HttpServletResponse response, @CookieValue(name = "userCookie", defaultValue = "None") String userCookie) {
        cookieService.setCookie(response, userCookie);
    }
}
