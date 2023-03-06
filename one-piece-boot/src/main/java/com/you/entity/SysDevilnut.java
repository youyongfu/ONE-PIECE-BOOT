package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 果实实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDevilnut extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //中文名
    private String name;

    //外文名
    private String foreignName;

    //别名
    private String alias;

    //类别
    private String category;

    //性质
    private String properties;

    //图片
    private String picture;

    //介绍
    private String introduce;

    //招式
    private String move;

    //子组织
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysDevilnut> children = new ArrayList<>();
}
