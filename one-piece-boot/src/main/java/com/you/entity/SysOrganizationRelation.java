package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 组织关系实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrganizationRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //关系类型
    private Integer relationType;

    //关联组织id
    private String relationOrganizationId;

    //简介
    private String synopsis;

    //所属组织id
    private String ownerId;

}
