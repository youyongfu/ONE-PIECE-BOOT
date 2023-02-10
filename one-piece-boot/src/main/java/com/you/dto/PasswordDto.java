package com.you.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 密码 dto类
 * @author yyf
 * @version 1.0
 * @date 2023/2/10
 */
@Data
public class PasswordDto implements Serializable {

    @NotBlank(message = "新密码不能为空")
    private String password;

    @NotBlank(message = "旧密码不能为空")
    private String currentPass;
}
