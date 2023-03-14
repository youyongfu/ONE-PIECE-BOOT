package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 武器实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysWeapon extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    //名称
    private String name;

    //外文名
    private String foreignName;

    //级别
    private String level;

    //价值
    private String money;

    //铸造者
    private String foundry;

    //使用者
    private String user;

    //简介
    private String synopsis;

    //来历
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String origin;

    //造型
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String modelling;

    //文件id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String fileIds;

}
