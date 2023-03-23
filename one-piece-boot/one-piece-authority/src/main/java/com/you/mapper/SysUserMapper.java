package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.SysUser;
import com.you.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysUser> getUserInfoByRoleId(Long roleId);

    List<SysUser> getUserInfoByMenuId(Long menuId);

    void deleteUserRoleByUserId(@Param("userIds") List userIds);

    void batcSaveUserRole(List<SysUserRole> userRoleList);
}
