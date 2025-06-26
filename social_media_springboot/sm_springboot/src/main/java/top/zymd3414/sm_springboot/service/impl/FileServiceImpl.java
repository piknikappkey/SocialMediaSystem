package top.zymd3414.sm_springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.zymd3414.sm_springboot.component.StaticResourceProperties;
import top.zymd3414.sm_springboot.pojo.Result;
import top.zymd3414.sm_springboot.pojo.UserIdEncryption;
import top.zymd3414.sm_springboot.service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private StaticResourceProperties staticResourceProperties;

    private String getFilePath(String... paths) {
        StringBuilder filePath = new StringBuilder();
        for (String path : paths) {
            filePath.append(path).append("/");
        }
        filePath.deleteCharAt(filePath.length() - 1);
        return filePath.toString();
    }

    @Override
    public Result forumSave(MultipartFile file, String name, String path, Integer userId) {
        // 如果查找不到用户
        if (userId == 0) return Result.error("查找不到用户，请重新登录");
        userId = UserIdEncryption.decode(userId);
        // 计算文件路径
        String filePath = getFilePath(staticResourceProperties.getStaticLocations(),
                "forum", userId.toString(), path, name);
        // 保存文件
        File dest = new File(filePath);
        System.out.println(dest.getAbsolutePath());
        try {
            file.transferTo(dest); // 将文件保存到目标路径
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    // 获取文件路径
    @Override
    public Result getPaths(String path) {
        List<String> files;
        try (Stream<Path> paths = Files.walk(Paths.get(staticResourceProperties.getStaticLocations(),path))) {
            files = paths
                    .filter(Files::isRegularFile)     // 只保留文件
                    .map(Path::getFileName)           // 提取文件名部分
                    .map(Path::toString)              // 转为字符串
                    .collect(Collectors.toList());
            files.removeLast();
            return Result.success(files);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
