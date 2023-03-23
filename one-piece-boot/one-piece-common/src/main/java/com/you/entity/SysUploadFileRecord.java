package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 上传文件记录实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUploadFileRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    private String id;

    //文件id
    private String fileId;

    //文件所有者id
    private String ownerId;

    //记录类型
    private String type;
}
