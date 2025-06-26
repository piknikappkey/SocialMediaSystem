package top.zymd3414.sm_springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zymd3414.sm_springboot.pojo.UserCode;
import java.time.LocalDateTime;

@Mapper
public interface CodeMapper {
    // 通过email查找
//    @Select("SELECT * FROM app_code WHERE email = #{email}")
    UserCode getUserCode(String email);

    // 写入验证码
//    @Select("INSERT INTO app_code (email, code, createdTime) VALUE (#{email}, #{code}, #{createdTime})")
    void addCode(String email, String code, LocalDateTime createdTime);

    // 更新验证码
//    @Select("UPDATE app_code SET code = #{code}, createdTime = #{createdTime} WHERE email = #{email}")
    void updateCode(String email, String code, LocalDateTime createdTime);
}
