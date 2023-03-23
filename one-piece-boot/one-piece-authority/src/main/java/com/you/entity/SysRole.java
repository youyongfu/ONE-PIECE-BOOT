package com.you.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //角色名称
    @NotBlank(message = "角色名称不能为空")
    private String name;

    //角色编码
    @NotBlank(message = "角色编码不能为空")
    private String code;

    //备注
    private String remark;

    //菜单id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<Long> menuIds = new ArrayList<>();
}
