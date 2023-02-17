package com.you.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
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

    //用户名
    @NotBlank(message = "用户名不能为空")
    private String username;

    //密码
    private String password;

    //头像
    private String avatar;

    //邮箱
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    //手机号
    private String phone;

    //籍贯
    private String city;

    //最后登录时间
    private Date lastLogin;

    //角色id
    @TableField(exist = false)    //表示当前属性不是数据库的字段
    private List<Long> roleIds = new ArrayList<>();
}
