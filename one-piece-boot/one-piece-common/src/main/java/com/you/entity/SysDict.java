package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    //父字典id
    private String parentId;

    //称
    private String name;

    //编码
    private String code;

    //值
    @TableField(value = "value")
    private String value;

    //子导航
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysDict> children = new ArrayList<>();

    //是否包含子节点
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private Boolean hasChildren;

}
