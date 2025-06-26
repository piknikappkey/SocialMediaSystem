package top.zymd3414.sm_springboot.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zymd3414.sm_springboot.pojo.ForumComment;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.service.ForumCommentService;

import java.util.Objects;

@RestController
public class ForumCommentController {
    @Autowired
    private ForumCommentService forumCommentService;

    @GetMapping("/comments")
    public Result getComments(@RequestParam Integer forumId,
                              @RequestParam Integer start,
                              @RequestParam Integer size,
                              @RequestParam String type,
                              @RequestParam(required = false) Integer fatherCommentId,
                              @RequestParam Integer userId) {
        if(start >= 0 && size >= 0 && Objects.equals(type, "Father")) return forumCommentService.getComments(forumId, start, size, userId);
        if(start >= 0 && size >= 0 && Objects.equals(type, "Child")) return forumCommentService.getChildComments(forumId, start, size, fatherCommentId, userId);
        return Result.error("error!");
    }
    @PostMapping("/comments")
    public Result addComments(@RequestBody ForumComment forumComment) {
        return forumCommentService.addComment(forumComment);
    }
}
