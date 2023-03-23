package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人物人际关系类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigureRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //关系类型
    private String relationType;

    //关系人
    private String relationFigureId;

    //关系简介
    private String relationSynopsis;

    //人物id
    private String figureId;

}
