package com.you.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.constant.OssConstant;
import com.you.entity.SysOrganizationFile;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysOrganizationMapper;
import com.you.mapper.SysUploadFileMapper;
import com.you.service.SysUploadFileService;
import com.you.utils.OssUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
    private SysOrganizationMapper sysOrganizationMapper;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @Override
    public SysUploadFile uploadFile(MultipartFile file,String type) {

        //上传文件
        String url = String.valueOf(ossUtils.upload(type,file));

        //保存文件信息
        String id = UUID.randomUUID().toString().replaceAll("-","");
        SysUploadFile sysUploadFile = new SysUploadFile();
        sysUploadFile.setId(id);
        sysUploadFile.setName(file.getOriginalFilename());
        sysUploadFile.setUrl(url);
        save(sysUploadFile);

        return sysUploadFile;
    }

    /**
     * 上传文件并保存记录
     */
    @Override
    public void uploadFileAndRecord(MultipartFile file, String type, String id) {

        //上传文件
        SysUploadFile sysUploadFile = uploadFile(file, type);

        //保存文件记录
        if(OssConstant.ORGANIZATION_TYPE.equals(type)){
            //添加组织文件关系
            SysOrganizationFile sysOrganizationFile = new SysOrganizationFile();
            sysOrganizationFile.setUploadFileId(sysUploadFile.getId());
            sysOrganizationFile.setOrganizationId(id);
            sysOrganizationMapper.saveOrganizationFile(sysOrganizationFile);
        }

    }
}
