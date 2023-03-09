package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.SysUploadFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据字典Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Mapper
public interface SysUploadFileMapper extends BaseMapper<SysUploadFile> {

    List<SysUploadFile> getFileByOrganizationId(String organizationId);

}
