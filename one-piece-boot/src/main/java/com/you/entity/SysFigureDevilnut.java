package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人物果实关系类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigureDevilnut implements Serializable {

    private static final long serialVersionUID = 1L;

    //人物id
    private String figureId;

    //果实id
    private String devilnutId;


}
