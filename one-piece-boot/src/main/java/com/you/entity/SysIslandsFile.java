package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 果实文件关系类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysIslandsFile implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    private String id;

    //岛屿id
    private String islandsId;

    //上传文件id
    private String uploadFileId;

}
