package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 剧集实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysEpisodes extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String id;

    //名称
    private String name;

    //BOSS
    private String boss;

    //简介
    private String synopsis;

    //登场角色
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<SysEpisodesCharacter> sysEpisodesCharacterList;

}
