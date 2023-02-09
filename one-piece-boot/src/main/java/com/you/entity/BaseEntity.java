package com.you.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基本实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
@Data
public class BaseEntity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //创建时间
    private LocalDateTime createdTime;

    //更新时间
    private LocalDateTime updatedTime;

    //状态
    private Integer statu;
}
