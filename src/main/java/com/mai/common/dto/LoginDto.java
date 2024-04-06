package com.mai.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {
    @NotBlank(message = "电话不能为空")
    private String userId;
    @NotBlank(message = "密码不能为空")
    private String userPassword;
    private String userEmail;
    private Boolean isAutoLogin;
}
