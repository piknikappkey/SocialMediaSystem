package top.zymd3414.sm_springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zymd3414.sm_springboot.pojo.Forum;
import java.util.List;

@Mapper
public interface ForumMapper {
    // 添加论坛数据
    void addForum(Forum forum);
    // 获取论坛数据
    List<Forum> getForums(Integer start, Integer size);
    // 获取个性化论坛数据
    List<Forum> getIndividuationForums(Integer start, Integer size, String feature);
}
