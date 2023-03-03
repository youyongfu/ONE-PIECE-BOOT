package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrganization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //父组织id
    private Long parentId;

    //中文名
    private String chineseName;

    //英文名
    private String englishName;

    //组织性质
    private String properties;

    //诞生时间
    private String birth;

    //组织驻地
    private String station;

    //最高权力
    private String supremePower;

    //组织标志
    private String sign;

    //组织历史
    private String history;


    //子组织
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysOrganization> children = new ArrayList<>();
}
