package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 剧集登场角色实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysEpisodesCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //人物id
    private String figureId;

    //剧集id
    private String episodesId;

    //简介
    private String synopsis;

}
