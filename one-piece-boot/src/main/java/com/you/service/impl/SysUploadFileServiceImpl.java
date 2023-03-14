package com.you.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.constant.OssConstant;
import com.you.entity.SysDevilnutFile;
import com.you.entity.SysOrganizationFile;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysUploadFileMapper;
import com.you.service.SysUploadFileService;
import com.you.utils.OssUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
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
            sysUploadFileMapper.saveOrganizationFileRecord(sysOrganizationFile);
        }else if(OssConstant.DEVILNUT_TYPE.equals(type)){
            //添加果实文件关系
            SysDevilnutFile sysDevilnutFile = new SysDevilnutFile();
            sysDevilnutFile.setUploadFileId(sysUploadFile.getId());
            sysDevilnutFile.setDevilnutId(id);
            sysUploadFileMapper.saveDevilnutFileRecord(sysDevilnutFile);
        }

    }

    /**
     * 获取文件保存记录
     * @param id
     * @return
     */
    @Override
    public List<SysUploadFile> getFileRecord(String type,String id) {
        List<SysUploadFile> fileList = new ArrayList<>();
        if(OssConstant.ORGANIZATION_TYPE.equals(type)){
            //组织文件保存记录
            fileList = sysUploadFileMapper.getOrganizationFileRecord(id);
        }else if(OssConstant.DEVILNUT_TYPE.equals(type)){
            //果实文件保存记录
            fileList = sysUploadFileMapper.getDevilnutFileRecord(id);
        }
        return fileList;
    }

    /**
     * 删除文件保存记录
     * @param type
     * @return
     */
    @Override
    public void deleteFileRecord(String type,List<String> fileIds) {
        List<SysUploadFile> fileList = new ArrayList<>();
        if(OssConstant.ORGANIZATION_TYPE.equals(type)){
            //删除组织文件保存记录
            fileList = sysUploadFileMapper.deleteOrganizationFileRecord(fileIds);
        }else if(OssConstant.DEVILNUT_TYPE.equals(type)){
            //删除果实文件保存记录
            fileList = sysUploadFileMapper.deleteDevilnutFileRecord(fileIds);
        }
    }
}
