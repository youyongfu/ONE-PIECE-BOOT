package com.you.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysUploadFileMapper;
import com.you.service.SysUploadFileService;
import com.you.utils.OssUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 上传文件服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/24
 */
@Service
public class SysUploadFileServiceImpl extends ServiceImpl<SysUploadFileMapper, SysUploadFile> implements SysUploadFileService {

    @Resource
    private OssUtils ossUtils;
    @Resource
    private SysUploadFileMapper sysUploadFileMapper;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @Override
    public SysUploadFile uploadFile(MultipartFile file,String type,Boolean preview) {

        //上传文件
        SysUploadFile sysUploadFile = ossUtils.upload(type, file);

        //保存文件信息
        String id = UUID.randomUUID().toString().replaceAll("-","");
        sysUploadFile.setId(id);
        sysUploadFile.setName(file.getOriginalFilename());
        save(sysUploadFile);

        return sysUploadFile;
    }

    /**
     * 获取文件保存记录
     * @param ownerId
     * @return
     */
    @Override
    public List<SysUploadFile> getFileInfo(String type,String ownerId) {
        return sysUploadFileMapper.getFileInfo(type,ownerId);
    }

}
