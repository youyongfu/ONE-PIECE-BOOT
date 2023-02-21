package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.SysRole;
import com.you.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> getRoleInfoByUserId(Long userId);

    void deleteRoleMenuByRoleId(@Param("roleIds") List roleIds);

    void deleteUserRoleByRoleId(@Param("roleIds") List roleIds);

    void batcSaveRoleMenu(List<SysRoleMenu> roleMenuList);
}
