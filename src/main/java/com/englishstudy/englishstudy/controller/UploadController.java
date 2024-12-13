package com.englishstudy.englishstudy.controller;

import com.englishstudy.englishstudy.model.FileType;
import com.englishstudy.englishstudy.service.OssUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss")
public class UploadController {

    @Autowired
    private OssUploader ossUploader;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") FileType type) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        // 调用 OssUploader 的 uploadFile 方法上传文件并存储元数据
        ossUploader.uploadFile(file, type);

        return "File upload request processed";
    }
}