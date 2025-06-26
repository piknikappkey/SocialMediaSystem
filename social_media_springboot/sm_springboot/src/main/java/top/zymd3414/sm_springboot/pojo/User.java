package top.zymd3414.sm_springboot.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private Integer userId;
    private String email;
    private String password;
    private String avatarUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    public User(String username, String email, String password) {
        Random random = new Random();
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatarUrl = "none" + random.nextInt(4);
        this.createdTime = LocalDateTime.now();
    }
}