package top.zymd3414.sm_springboot.service;

import org.springframework.web.multipart.MultipartFile;
import top.zymd3414.sm_springboot.pojo.Result;

public interface FileService {
    Result forumSave(MultipartFile file, String name, String path, Integer userId);

    Result getPaths(String path);
}
