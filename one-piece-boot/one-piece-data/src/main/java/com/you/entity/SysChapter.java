package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 篇章实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysChapter extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String id;

    //名称
    private String name;

    //篇章开始剧集
    private String beginEpisodesId;

    //篇章结束剧集
    private String endEpisodesId;

    //简介
    private String synopsis;

    //篇章内容
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String content;

    //所属章节
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysChapterSections> sysChapterSectionsList;

}
