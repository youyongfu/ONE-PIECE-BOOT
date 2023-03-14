package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.SysDevilnutFile;
import com.you.entity.SysOrganizationFile;
import com.you.entity.SysUploadFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 上传文件Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Mapper
public interface SysUploadFileMapper extends BaseMapper<SysUploadFile> {

    void saveOrganizationFileRecord(SysOrganizationFile sysOrganizationFile);

    List<SysUploadFile> getOrganizationFileRecord(String organizationId);

    List<SysUploadFile> deleteOrganizationFileRecord(List<String> fileIds);

    void saveDevilnutFileRecord(SysDevilnutFile sysDevilnutFile);

    List<SysUploadFile> getDevilnutFileRecord(String devilnutId);

    List<SysUploadFile> deleteDevilnutFileRecord(List<String> fileIds);
}
