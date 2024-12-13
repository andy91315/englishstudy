package com.englishstudy.englishstudy.service;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.englishstudy.englishstudy.model.File;
import com.englishstudy.englishstudy.model.FileType;
import com.englishstudy.englishstudy.repository.FileRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OssUploader {

    private static final Logger logger = LogManager.getLogger(OssUploader.class);
    private final String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private final String region = "cn-hangzhou";

    // OSS客户端实例
    private OSS ossClient;

    // 文件存储库
    @Autowired
    private FileRepository fileRepository;

    public OssUploader() {
        // 从环境变量中获取访问凭证
        EnvironmentVariableCredentialsProvider credentialsProvider = new EnvironmentVariableCredentialsProvider();
        
        // 创建OSSClient实例
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);  
        this.ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
    }

    public void uploadFile(MultipartFile multipartFile, FileType type) {
        String fileName = multipartFile.getOriginalFilename();
        java.io.File tempFile = null;
        String bucketName = getBucketNameByFileType(type);
        String dateFolder = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String objectName = dateFolder + "/" + fileName;
        try {
            // 将 MultipartFile 转换为 File
            tempFile = java.io.File.createTempFile("temp", null);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(multipartFile.getBytes());
            }

            // 上传文件到 OSS
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, tempFile);
            ossClient.putObject(putObjectRequest);
            
            // 构建文件的访问链接
            String fileUrl = "https://" + bucketName + "." + endpoint.replace("https://", "") + "/" + objectName;

            // 存储文件元数据
            File file = new File();
            file.setFileName(fileName);
            file.setOssUrl(fileUrl);
            file.setType(type);
            fileRepository.save(file);

            logger.info("File uploaded successfully. URL: " + fileUrl);
        } catch (OSSException | ClientException | IOException e) {
            logger.error("File upload failed", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /**
     * 根据文件类型获取对应的存储桶名称
     * @param type 文件类型
     * @return
     */
    private String getBucketNameByFileType(FileType type) {
        switch (type) {
            case IMAGE:
                return "englishstudy-image";
            case VIDEO:
                return "englishstudy-video";
            case AUDIO:
                return "englishstudy-audio";
            default:
                throw new IllegalArgumentException("Unsupported file type: " + type);
        }
    }
}