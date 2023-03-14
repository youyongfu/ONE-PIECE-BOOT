package com.you.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基本实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
@Data
public class BaseEntity implements Serializable {

    //创建时间
    private Date createdTime;

    //更新时间
    private Date updatedTime;

    //状态
    private Integer statu;
}
