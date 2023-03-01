package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

}
