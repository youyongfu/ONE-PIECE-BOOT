package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 组织实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //父组织id
    private String parentId;

    //中文名
    private String name;

    //外文名
    private String foreignName;

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

    //创建时间
    private Date createdTime;

    //更新时间
    private Date updatedTime;

    //状态
    private Integer statu;

    //子组织
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysOrganization> children = new ArrayList<>();

    //文件id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String fileIds;
}
