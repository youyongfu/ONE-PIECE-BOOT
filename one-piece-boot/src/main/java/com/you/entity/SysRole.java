package com.you.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 角色实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //角色名称
    @NotBlank(message = "角色名称不能为空")
    private String name;

    //角色编码
    @NotBlank(message = "角色编码不能为空")
    private String code;

    //备注
    private String remark;

}
