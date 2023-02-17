package com.you.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单关系类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    //角色id
    private Long roleId;

    //菜单id
    private Long menuId;

}
