package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人物经历类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigureExperience implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //活跃时间
    private String activeTime;

    //组织id
    private String organizationId;

    //身份
    private String position;

    //船只id
    private String shippingId;

    //人物id
    private String figureId;

}
