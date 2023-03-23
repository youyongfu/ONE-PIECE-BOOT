package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.entity.SysUploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 上传文件服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/24
 */
public interface SysUploadFileService extends IService<SysUploadFile> {

    SysUploadFile uploadFile(MultipartFile file,String type,Boolean preview);

    List<SysUploadFile> getFileInfo(String type,String id);

}
