package com.mai.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@TableName("user")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号 不重复
     */
    private String userId;

    /**
     * 是否封禁 0否 1是
     */
    private Integer userFlag;

    /**
     * 昵称 不重复
     */
    private String userNickname;

    private String userPassword;

    private String userEmail;

    private String userHead;

    /**
     * 0用户 1申请商家中 2商家
     */
    private Integer userStatus;

    /**
     * 是否vip 0否 1是
     */
    private Integer userIsvip;

    /**
     * 用户自我介绍
     */
    private String userIntro;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
