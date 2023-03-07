package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 人物实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigure extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //名称
    private String name;

    //外文名
    private String foreignName;

    //别名
    private String alias;

    //生日
    private Date birth;

    //年龄
    private Integer age;

    //血型
    private String blood;

    //身高
    private String height;

    //性别
    private Integer sex;

    //悬赏金
    private String bounty;

    //霸气
    private String overbearing;

    // 简介
    private String synopsis;

    //角色背景
    private String source;

    //形象
    private String image;

    //日常生活
    private String life;

    //角色能力
    private String ability;

    //经历
    private String experience;

    //对战记录
    private String warRecord;

    //图片
    private String picture;


}
