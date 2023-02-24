package com.you.service.impl;

import com.you.common.ResultBean;
import com.you.constant.OssConstant;
import com.you.entity.SysUser;
import com.you.service.SysUserService;
import com.you.service.UploadFileService;
import com.you.utils.OssUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 上传文件服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/24
 */
@Service
public class UploadFileServiceImpl implements UploadFileService {

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
        String url = String.valueOf(ossUtils.upload(username, OssConstant.CLASSIFY_AVATAR,file));
        sysUser.setAvatar(url);
        sysUserService.updateById(sysUser);

        return ResultBean.success(url);
    }
}
