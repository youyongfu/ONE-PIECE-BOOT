package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人物组织关系类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigureOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    //人物id
    private String figureId;

    //组织id
    private String organizationId;


}
