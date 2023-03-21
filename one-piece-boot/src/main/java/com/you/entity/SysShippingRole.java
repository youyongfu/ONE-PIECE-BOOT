package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 船只相关角色类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysShippingRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //身份类型
    private Integer relationIdentity;

    //关系类型
    private String relation;

    //关系人/关系组织
    private String relationId;

    //船只id
    private String shippingId;

    //简介
    private String synopsis;

}
