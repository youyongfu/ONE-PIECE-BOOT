package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 岛屿实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysIslands extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //父组织id
    private Long parentId;

    //中文名
    private String name;

    //外文名
    private String foreignName;

    //别名
    private String alias;

    //位置
    private String seat;

    //历史
    private String history;

    //来源
    private String source;

    //图片
    private String picture;

    //地理
    private String geography;

    //文化
    private String civilization;


    //子区域
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysIslands> children = new ArrayList<>();
}
