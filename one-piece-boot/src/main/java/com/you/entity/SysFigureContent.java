package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人物内容实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigureContent implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    private String id;

    //人物id
    private String figureId;

    //内容
    private String content;

    //类型
    private String type;

}
