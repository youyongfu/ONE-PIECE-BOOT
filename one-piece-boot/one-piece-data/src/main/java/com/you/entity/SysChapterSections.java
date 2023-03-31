package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 章节实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysChapterSections implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //名称
    private String name;

    //章节开始剧集
    private String beginEpisodesId;

    //章节结束剧集
    private String endEpisodesId;

    //篇章id
    private String chapterId;

    //排序号
    private Integer sortNumber;

}
