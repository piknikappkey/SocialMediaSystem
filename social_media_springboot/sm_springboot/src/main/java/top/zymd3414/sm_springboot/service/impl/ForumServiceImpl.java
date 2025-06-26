package top.zymd3414.sm_springboot.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zymd3414.sm_springboot.component.StaticResourceProperties;
import top.zymd3414.sm_springboot.mapper.CookieMapper;
import top.zymd3414.sm_springboot.mapper.ForumMapper;
import top.zymd3414.sm_springboot.mapper.ForumNumberMapper;
import top.zymd3414.sm_springboot.pojo.Forum;
import top.zymd3414.sm_springboot.pojo.ForumNumber;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.pojo.UserIdEncryption;
import top.zymd3414.sm_springboot.service.ForumService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumServiceImpl implements ForumService {
    @Autowired
    CookieMapper cookieMapper;
    @Autowired // 论坛数据库
    ForumMapper forumMapper;
    @Autowired // 论坛计数数据库
    ForumNumberMapper forumNumberMapper;
    @Autowired // 用于获取保存路径
    private StaticResourceProperties staticResourceProperties;
     // 默认存储路径
    // 根据内容返回喜好
    private String getForumFeature(String content) {
        return "";
    }

    // 新建论坛
    @Override
    public Result create(Forum forum) {
        // 如果查找不到用户
        if (forum.getUserId() == 0) return Result.error("查找不到用户，请重新登录");
        forum.setUserId(UserIdEncryption.decode(forum.getUserId()));
        // 开始新建论坛逻辑
        // 修正内容文件夹
        forum.setForumContentUrl("forum/" + forum.getUserId() + "/" + forum.getForumContentUrl());
        // 补充创建时间
        forum.setCreatedTime(LocalDateTime.now());
        // 根据时间创建文件夹
        Path storageDir = Paths.get(staticResourceProperties.getStaticLocations() +  forum.getForumContentUrl());
        try {
            Files.createDirectories(storageDir);
        } catch (java.io.IOException e) { // 错误提示
            System.err.println(e.getMessage());
            return Result.error(e.getMessage());
        }
        // 写入数据到forum数据库
        forumMapper.addForum(forum);
        ForumNumber forumNumber = new ForumNumber(forum);
        forumNumberMapper.addForumNumber(forumNumber);
        return Result.success();
    }
    // 获取论坛
    @Override
    public Result getForums(Integer start, Integer size, Integer userId) {
        // 如果查找不到用户
        if (userId == null || userId == 0) {
            List<Forum> forums = forumMapper.getForums(start, size);
            for (Forum forum : forums) {
                forum.setUserId(UserIdEncryption.encrypt(forum.getUserId()));
            }
            // 返回大众化的论坛
            return Result.success(forums);
        };
        userId = UserIdEncryption.decode(userId);
        // 如果查找到了用户
        // 通过用户个性化表查询用户个性化特征（暂未实现）
        // 返回个性化论坛（暂未实现，用大众化论坛替代）
        // 用户id加密
        List<Forum> forums = forumMapper.getForums(start, size);
        for (Forum forum : forums) {
            forum.setUserId(UserIdEncryption.encrypt(forum.getUserId()));
        }
        return Result.success(forums);
    }

    @Override
    public Result getForum(Integer forumId, Integer userId) {

        return null;
    }
}
