package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //菜单ID，一级菜单为0
    private Long parentId;

    //菜单名称
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    //菜单URL
    private String path;

    //授权(多个用逗号分隔，如：user:list,user:create)
    @NotBlank(message = "菜单授权码不能为空")
    private String perms;

    //组件
    private String component;

    //类型     0：目录   1：菜单   2：按钮
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    //菜单图标
    private String icon;

    //排序
    private Integer orderNum;

    //子导航
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysMenu> children = new ArrayList<>();

    //是否包含子节点
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private Boolean hasChildren;
}

