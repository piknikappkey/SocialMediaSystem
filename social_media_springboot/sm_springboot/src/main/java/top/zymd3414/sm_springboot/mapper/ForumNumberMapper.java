package top.zymd3414.sm_springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zymd3414.sm_springboot.pojo.ForumNumber;

@Mapper
public interface ForumNumberMapper {
    // 添加论坛计数数据
    void addForumNumber(ForumNumber forumNumber);
}
