package com.you.service;

import com.you.common.ResultBean;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/24
 */
public interface UploadFileService {

    ResultBean uploadAvatar(MultipartFile file,String username);
}
