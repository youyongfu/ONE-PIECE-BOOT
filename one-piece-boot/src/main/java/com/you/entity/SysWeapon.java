package com.you.entity;

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

    //中文名
    private String name;

    //级别
    private String level;

    //价值
    private String cost;

    //特点
    private String point;

    //图片
    private String picture;

    //介绍
    private String introduce;

}
