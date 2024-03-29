package com.sandy.fw.admin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "原密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度为6-20位")
    private String password;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度为6-20位")
    private String newPassword;
}
