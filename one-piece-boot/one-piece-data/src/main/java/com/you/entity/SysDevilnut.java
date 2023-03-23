package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 果实实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDevilnut extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String id;

    //名称
    private String name;

    //外文名
    private String foreignName;

    //别名
    private String alias;

    //类别
    private String category;

    //性质
    private String nature;

    //初次出现
    private String debut;

    //简介
    private String synopsis;

    //果实能力
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String ability;

    //果实招式
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String move;

    //文件id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String fileIds;

}
