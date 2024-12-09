package com.englishstudy.englishstudy.controller;

import com.englishstudy.englishstudy.service.OssUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/oss")
public class UploadController {

    @Autowired
    private OssUploader ossUploader;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        try {
            // 将 MultipartFile 转换为 File
            File tempFile = File.createTempFile("temp", null);
            file.transferTo(tempFile);

            // 调用 OssUploader 的 uploadFile 方法上传文件
            ossUploader.uploadFile(tempFile.getAbsolutePath());

            // 删除临时文件
            tempFile.delete();

            return "File uploaded successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed";
        }
    }
}