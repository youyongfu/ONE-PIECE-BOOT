package com.you.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色关系类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    //角色id
    private String roleId;

    //用户id
    private String userId;

}
