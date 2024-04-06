package com.mai.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Cmt implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "cmt_id", type = IdType.ASSIGN_ID)
    private Long cmtId;

    /**
     * 评分1~5
     */
    private Integer cmtScore;

    /**
     * 评论内容
     */
    private String cmtText;

    /**
     * 用户是否选择匿名评论 0否 1是
     */
    private Integer cmtIsanon;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户头像
     */
    private String userHead;

    /**
     * 评论的产品id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    /**
     * 用户评论返图
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long imgsId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
