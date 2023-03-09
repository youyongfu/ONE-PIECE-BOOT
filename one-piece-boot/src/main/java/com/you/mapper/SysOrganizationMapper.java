package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.SysOrganization;
import com.you.entity.SysOrganizationFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 组织管理Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Mapper
public interface SysOrganizationMapper extends BaseMapper<SysOrganization> {

    void saveOrganizationFile(SysOrganizationFile sysOrganization);

    void deleteOrganizationFileByFileId(List<String> fileIds);
}
