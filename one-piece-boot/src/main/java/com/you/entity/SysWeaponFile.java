package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 武器文件关系类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysWeaponFile implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    private String id;

    //船只id
    private String weaponId;

    //上传文件id
    private String uploadFileId;

}
