package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrganization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    //父组织id
    private String parentId;

    //中文名
    private String name;

    //外文名
    private String foreignName;

    //组织性质
    private Integer nature;

    //诞生时间
    private String birth;

    //领导者
    private String leader;

    //总部
    private String headquarters;

    //初次登场
    private String debut;

    //简介
    private String synopsis;

    //成立背景
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String background;

    //组织经历
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String experience;

    //组织文化
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String civilization;

    //组织成员
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String member;

    //组织驻地
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String station;

    //组织关系
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String relation;

    //子组织
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysOrganization> children = new ArrayList<>();

    //文件id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String fileIds;

    //组织关系
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysOrganizationRelation> sysOrganizationRelationList = new ArrayList<>();
}
