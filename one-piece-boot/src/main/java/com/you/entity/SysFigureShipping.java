package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人物船只关系类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigureShipping implements Serializable {

    private static final long serialVersionUID = 1L;

    //人物id
    private String figureId;

    //船只id
    private String shippingId;


}
