package top.zymd3414.sm_springboot.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zymd3414.sm_springboot.pojo.Forum;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.service.ForumService;

@RestController
public class ForumController {
    @Autowired
    private ForumService forumService;

    // 获取论坛
    @GetMapping("/forums")
    public Result getForum(
            @RequestParam Integer start,
            @RequestParam Integer size,
            @RequestParam(required = false) Integer userId) {
        if(start >= 0 && size >= 0) return forumService.getForums(start, size, userId);
        return Result.error("error!");
    }
    // 发布论坛（获取论坛标签）
    @PostMapping("/forums")
    public Result postForum(@RequestBody Forum forum) {
        return forumService.create(forum);
    }
    // 删除论坛
    @DeleteMapping("/forums")
    public Result deleteForum(@RequestParam Integer userId){
        return Result.success();
    }
}
