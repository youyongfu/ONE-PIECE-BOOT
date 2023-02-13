package com.you.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yyf
 * @version 1.0
 * @date 2023/2/13
 */
@Data
public class SysMenuDto implements Serializable {

    //ID
    private Long id;

    //导航名
    private String name;

    //导航图标
    private String icon;

    //跳转路径
    private String path;

    //授权
    private String perms;

    //子导航
    private List<SysMenuDto> children = new ArrayList<>();

}
