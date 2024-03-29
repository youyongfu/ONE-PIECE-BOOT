package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 人物实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFigure implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //名称
    private String name;

    //外文名
    private String foreignName;

    //别名
    private String alias;

    //性别
    private String sex;

    //生日
    private String birth;

    //年龄
    private String age;

    //血型
    private String blood;

    //身高
    private String height;

    //悬赏金
    private String bounty;

    //初次登场
    private String debut;

    //职位
    private String position;

    //饮食习惯
    private String eatingHabits;

    //霸气
    private String overbearing;

    //星座
    private String constellation;

    //正义观
    private String justiceView;

    //隶属组织
    private String organizationId;

    // 简介
    private String synopsis;

    //状态
    private String statu;

    //创建时间
    private Date createdTime;

    //更新时间
    private Date updatedTime;

    //恶魔果实
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String devilnutIds;

    //所属组织
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String organizationIds;

    //所属岛屿
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String islandsIds;

    //所属船只
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String shippingIds;

    //所属武器
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String weaponIds;

    //角色背景
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String background;

    //角色形象
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String image;

    //角色生活
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String life;

    //角色能力
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String ability;

    //角色经历
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String experience;

    //对战记录
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String warRecord;

    //文件id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private String fileIds;

    //经历
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysFigureExperience> sysFigureExperienceList;

    //人际关系
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysFigureRelation> sysFigureRelationList;

    //对战记录
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysFigureWarRecord> sysFigureWarRecordList;

    //悬赏变化
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysFigureReward> sysFigureRewardList;

    //搭乘船只
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysFigureShipping> sysFigureShippingList;
}
