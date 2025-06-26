package top.zymd3414.sm_springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.service.FileService;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    // 请求静态文件
    @GetMapping("/files")
    public Result getFiles() {
        return Result.success();
    }
    // 接收静态文件
    @PostMapping("/files")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("name") String name,
                             @RequestParam("path") String path,
                             @RequestParam("type") String type,
                             @RequestParam("userId") Integer userId) {
        return switch (type) {
            case "forum" -> fileService.forumSave(file, name, path, userId); // 接收论坛文件
            default -> Result.error("错误的文件接收请求！");
        };
    }

    // 请求文件路径
    @GetMapping("/paths")
    public Result getPaths(@RequestParam("path") String path) {
        return fileService.getPaths(path);
    }
}
