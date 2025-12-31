package com.sandy.fw.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "原密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度大于6")
    private String password;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度大于6")
    private String newPassword;
}
