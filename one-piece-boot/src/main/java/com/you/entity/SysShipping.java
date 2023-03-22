package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 船只实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysShipping implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //名称
    private String name;

    //外文名
    private String foreignName;

    //别名
    private String alias;

    //型号
    private String model;

    //建造日
    private String bulidDate;

    //全长
    private String length;

    //总高
    private String height;

    //设计者
    private String designer;

    //建造者
    private String producer;

    //使用者
    private String user;

    //简介
    private String synopsis;

    //状态
    private String statu;

    //创建时间
    private Date createdTime;

    //更新时间
    private Date updatedTime;

    //背景
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String background;

    //外观
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String appearance;

    //功能
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String function;

    //经历
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String experience;

    //文件id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String fileIds;

    //相关角色
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysShippingRole> sysShippingRoleList;

}
