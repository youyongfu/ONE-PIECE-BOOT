package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人物对战记录关系表
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigureWarRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //对战对手
    private String opponentFigureId;

    //对战结果
    private String battleResults;

    //简介
    private String synopsis;

    //人物id
    private String figureId;

    //排序号
    private Integer sortNumber;
}
