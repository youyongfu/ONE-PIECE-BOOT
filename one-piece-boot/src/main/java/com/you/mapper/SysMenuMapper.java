package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> getMenuPermsByUserId(Long userId);
}
