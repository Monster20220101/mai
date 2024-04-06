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
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 使用fastjson的@JsonSerialize，解决：
     * 后端long类型传给前端Number类型，超过17位发生溢出 的问题
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "address_id", type = IdType.ASSIGN_ID)
    private Long addressId;

    /**
     * 地址所属区域 省-市-区县乡
     */
    private String addressRegion;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 收货人姓名
     */
    private String addressName;

    /**
     * 收货人电话
     */
    private String addressTel;

    /**
     * 类型 家/学校/公司
     */
    private String addressType;

    /**
     * 是否为默认地址 0否 1是
     */
    private Integer addressIsdefault;

    private String userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;


}
