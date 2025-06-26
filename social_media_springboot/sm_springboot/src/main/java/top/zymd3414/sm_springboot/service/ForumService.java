package top.zymd3414.sm_springboot.service;

import top.zymd3414.sm_springboot.pojo.Forum;
import top.zymd3414.sm_springboot.pojo.Result;

public interface ForumService {
    // 创建论坛表单并写入数据库
    Result create(Forum forum);

    // 获取论坛表单
    Result getForums(Integer start, Integer size, Integer userId);

    // 获取单个论坛（详细数据）
    Result getForum(Integer forumId, Integer userId);
}
