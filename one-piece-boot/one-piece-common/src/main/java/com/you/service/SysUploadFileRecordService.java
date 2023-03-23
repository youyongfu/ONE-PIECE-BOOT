package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.entity.SysUploadFileRecord;

import java.util.List;

/**
 * 上传文件记录服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/24
 */
public interface SysUploadFileRecordService extends IService<SysUploadFileRecord> {

    void saveFileRecord(String type, String fileId, String ownerId);

    void deleteFileRecord(String type, List<String> fileIds);
}
