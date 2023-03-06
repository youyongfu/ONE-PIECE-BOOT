package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 船只实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysShipping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //中文名
    private String name;

    //外文名
    private String foreignName;

    //别名
    private String alias;

    //建造日
    private String birth;

    //总高
    private String height;

    //全长
    private String weight;

    //型号
    private String model;

    //设计者
    private String designer;

    //建造者
    private String manufacturer;

    //图片
    private String picture;

    //功能
    private String function;

    //经历
    private String experience;

}
