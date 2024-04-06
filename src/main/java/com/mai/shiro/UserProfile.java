package com.mai.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * 是登录成功之后返回的一个用户信息的载体
 */
@Data
public class UserProfile implements Serializable {
    private String userId;
    private String userNickname;
    private Integer userStatus;
    private String userHead;
}