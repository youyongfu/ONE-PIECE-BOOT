package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 大字段内容实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysClobContent implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    private String id;

    //所有者id
    private String ownerId;

    //内容
    private String content;

    //类型
    private String type;

}
