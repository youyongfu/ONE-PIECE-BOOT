package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysUploadFile;
import com.you.service.SysUploadFileRecordService;
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
    @Resource
    private SysUploadFileRecordService uploadFileRecordService;

    /**
     * 上传文件
     */
    @ApiOperation("上传文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAuthority('sys:user:upload')")
    public ResultBean uploadFile(MultipartFile file,String type) {
        return ResultBean.success(uploadFileService.uploadFile(file,type));
    }

    /**
     * 上传文件并保存记录
     */
    @ApiOperation("上传文件并保存记录")
    @PostMapping("/uploadFileAndRecord")
    @PreAuthorize("hasAuthority('sys:user:upload')")
    public ResultBean uploadFileAndRecord(MultipartFile file,String type,String id) {
        //上传文件
        SysUploadFile sysUploadFile = uploadFileService.uploadFile(file, type);
        //保存文件记录
        uploadFileRecordService.saveFileRecord(type,sysUploadFile.getId(),id);
        return ResultBean.success();
    }

}

