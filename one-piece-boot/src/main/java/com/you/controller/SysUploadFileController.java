package com.you.controller;

import com.you.common.ResultBean;
import com.you.service.SysUploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;

/**
 * 上传文件控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/24
 */
@Api(tags = "上传文件控制层")
@Valid
@RestController
@RequestMapping("/file")
public class SysUploadFileController {

    @Resource
    private SysUploadFileService uploadFileService;

    /**
     * 上传头像
     */
    @ApiOperation("上传头像")
    @PostMapping("/uploadAvatar")
    @PreAuthorize("hasAuthority('sys:user:upload')")
    public ResultBean uploadAvatar(MultipartFile file, Principal principal) {
        return uploadFileService.uploadAvatar(file,principal.getName());
    }

    /**
     * 上传文件
     */
    @ApiOperation("上传文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAuthority('sys:user:upload')")
    public ResultBean uploadFile(MultipartFile file) {
        return uploadFileService.uploadFile(file);
    }

}

