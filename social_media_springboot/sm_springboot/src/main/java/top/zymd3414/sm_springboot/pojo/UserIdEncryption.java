package top.zymd3414.sm_springboot.pojo;

public class UserIdEncryption {
    public static Integer encrypt(Integer userId) {
        // 加密。。。
        return userId + 10000;
    }
    public static Integer decode(Integer userId) {
        // 解密。。。
        return userId - 10000;
    }
}
