package top.zymd3414.sm_springboot.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyUser {
    private Integer logMode;
    private String email;
    private String password;
    private String password2;
    private String code;
}
