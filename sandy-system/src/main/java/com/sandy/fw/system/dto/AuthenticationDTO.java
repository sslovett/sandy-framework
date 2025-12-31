package com.sandy.fw.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationDTO {

    @NotBlank(message = "账号不能为空")
    public String userName;

    @NotBlank(message = "密码不能为空")
    public String password;


}
