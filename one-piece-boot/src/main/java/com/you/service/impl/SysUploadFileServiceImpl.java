package com.you.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.constant.OssConstant;
import com.you.entity.SysUploadFile;
import com.you.entity.SysUser;
import com.you.mapper.SysUploadFileMapper;
import com.you.service.SysUploadFileService;
import com.you.service.SysUserService;
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
    private SysUserService sysUserService;

    /**
     * 上传头像
     * @param file
     * @return
     */
    @Override
    public ResultBean uploadAvatar(MultipartFile file,String username) {

        SysUser sysUser = sysUserService.getByUsername(username);

        //上传新头像
        String url = String.valueOf(ossUtils.upload(OssConstant.CLASSIFY_AVATAR,file));
        sysUser.setAvatar(url);
        sysUserService.updateById(sysUser);

        return ResultBean.success(url);
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @Override
    public ResultBean uploadFile(MultipartFile file) {

        //上传文件
        String url = String.valueOf(ossUtils.upload(OssConstant.CLASSIFY_SIGN,file));

        //保存文件信息
        String id = UUID.randomUUID().toString().replaceAll("-","");
        SysUploadFile sysUploadFile = new SysUploadFile();
        sysUploadFile.setId(id);
        sysUploadFile.setName(file.getOriginalFilename());
        sysUploadFile.setUrl(url);
        save(sysUploadFile);

        return ResultBean.success(sysUploadFile);
    }
}
