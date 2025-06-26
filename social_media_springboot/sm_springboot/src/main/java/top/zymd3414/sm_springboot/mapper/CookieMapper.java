package top.zymd3414.sm_springboot.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;

@Mapper
public interface CookieMapper {
    // 通过cookie表获取用户id
//    @Select("SELECT userId FROM app_cookie WHERE cookie = #{cookie}")
    Integer getUserId(String cookie);
    // 通过cookie删除行
//    @Select("DELETE FROM app_cookie WHERE cookie = #{cookie}")
    void delByCookie(String cookie);
    // 通过id删除行
    void delById(Integer id);
    // 增加行
//    @Select("INSERT INTO app_cookie (userId, cookie, createdTime) VALUES (#{id}, #{cookie}, #{createdTime})")
    void addCookie(Integer id, String cookie, LocalDateTime createdTime);
    // 更新日期
//    @Select("UPDATE app_cookie SET createdTime = #{createdTime} WHERE cookie = #{cookie}")
    void update(String cookie, LocalDateTime createdTime);
}
