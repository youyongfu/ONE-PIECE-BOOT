package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 上传文件实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    private String id;

    //文件名
    private String name;

    //文件地址
    private String url;

    //文件夹地址
    private String foldName;
}
