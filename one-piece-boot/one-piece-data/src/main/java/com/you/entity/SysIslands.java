package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 岛屿实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysIslands extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    //父组织id
    private String parentId;

    //中文名
    private String name;

    //外文名
    private String foreignName;

    //别名
    private String alias;

    //地理位置
    private String position;

    //特征
    private String characteristic;

    //气候条件
    private String climate;

    //初次出现
    private String debut;

    //简介
    private String synopsis;

    //历史
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String source;

    //地理环境
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String geography;

    //登场人物
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String appearances;

    //子区域
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysIslands> children = new ArrayList<>();

    //文件id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String fileIds;
}
