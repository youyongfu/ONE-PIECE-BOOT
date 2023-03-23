package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 时间线实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysTimeline extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //时间节点
    private String stage;

    //事件
    private String content;

    //排序号
    private Integer orderNum;

}
