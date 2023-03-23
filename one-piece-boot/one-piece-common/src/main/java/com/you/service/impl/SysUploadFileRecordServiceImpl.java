package com.you.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.entity.SysUploadFileRecord;
import com.you.mapper.SysUploadFileRecordMapper;
import com.you.service.SysUploadFileRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 上传文件记录服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/24
 */
@Service
public class SysUploadFileRecordServiceImpl extends ServiceImpl<SysUploadFileRecordMapper, SysUploadFileRecord> implements SysUploadFileRecordService {

    /**
     * 保存文件记录
     * @param type
     * @param fileId
     * @param ownerId
     */
    @Override
    public void saveFileRecord(String type,String fileId, String ownerId) {
        SysUploadFileRecord sysUploadFileRecord = new SysUploadFileRecord();
        sysUploadFileRecord.setId(UUID.randomUUID().toString().replaceAll("-",""));
        sysUploadFileRecord.setFileId(fileId);
        sysUploadFileRecord.setOwnerId(ownerId);
        sysUploadFileRecord.setType(type);
        save(sysUploadFileRecord);
    }

    /**
     * 删除文件记录
     * @param type
     * @param fileIds
     */
    @Override
    public void deleteFileRecord(String type, List<String> fileIds) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type",type);
        queryWrapper.in("file_id",fileIds);
        remove(queryWrapper);
    }
}
