package com.you.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.ObjectMetadata;
import com.you.constant.OssConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件上传工具类
 * @author yyf
 * @version 1.0
 * @date 2023/2/24
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssUtils {

    private String host;

    private String bucketName;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    /**
     * 上传文件
     * @param file
     * @return
     */
    public String upload(String classify,MultipartFile file) {

        if (file.isEmpty()) {
            throw new ServiceException("上传文件不能为空");
        }
        if (!checkFileSize(file.getSize(), OssConstant.LEN, OssConstant.UNIT_M)) {
            throw new ServiceException("上传文件大小不能超过10M");
        }

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 获取文件的名称
            String fileName = file.getOriginalFilename();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));

            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            fileName = classify + "/" + date + "/" + fileName;
            // 调用oss的方法实现长传 第一个参数 bucketName,第二个参数 上传到oss的文件路径和文件名称
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(file.getBytes()),objectMetadata);
            // 关闭OSSClient。
            ossClient.shutdown();
            // 这里设置图片有效时间 我设置了30年
            Date expiration = new Date(System.currentTimeMillis() + 946080000 * 1000);
            String url = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验文件
     * @param len
     * @param size
     * @param unit
     * @return
     */
    public boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if (OssConstant.UNIT_B.equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if (OssConstant.UNIT_K.equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if (OssConstant.UNIT_M.equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if (OssConstant.UNIT_G.equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }

    // 实现图片的预览功能
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }

}

