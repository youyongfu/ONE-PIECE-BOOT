package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    //用户名
    @NotBlank(message = "用户名不能为空")
    private String username;

    //密码
    private String password;

    //上传文件id
    private String uploadFileId;

    //邮箱
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    //手机号
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式不正确")
    private String phone;

    //籍贯
    private String city;

    //角色id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<String> roleIds = new ArrayList<>();
}
