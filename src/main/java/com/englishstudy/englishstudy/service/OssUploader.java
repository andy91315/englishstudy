package com.englishstudy.englishstudy.service;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import java.io.File;
import org.springframework.stereotype.Service;

@Service
public class OssUploader {

    private OSS ossClient;
    private final String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private final String bucketName = "englishstudy-video";
    private final String region = "cn-hangzhou";

    public OssUploader() throws com.aliyuncs.exceptions.ClientException {
        // 打印环境变量以进行调试
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");
        System.out.println("OSS_ACCESS_KEY_ID: " + accessKeyId);
        System.out.println("OSS_ACCESS_KEY_SECRET: " + accessKeySecret);
        
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        
        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);  
        this.ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
    }

    public void uploadFile(String filename, String filePath) {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = new File(filePath).getName();

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(filePath));
            // 上传文件。
            ossClient.putObject(putObjectRequest);
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
        }
    }
}