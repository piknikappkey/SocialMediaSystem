package top.zymd3414.sm_springboot.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer state; // 1成功、0失败
    private String content; // 错误信息
    private Object data; // 返回数据

    public static Result success() {
        Result result = new Result();
        result.state = 1;
        result.content = "success";
        return result;
    }
    public static Result success(String content) {
        Result result = new Result();
        result.state = 1;
        result.content = content;
        return result;
    }
    public static Result success(Object data) {
        Result result = success();
        result.data = data;
        return result;
    }
    public static Result error(String content) {
        Result result = new Result();
        result.state = 0;
        result.content = content;
        return result;
    }
}
