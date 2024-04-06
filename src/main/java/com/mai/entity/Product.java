package com.mai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "product_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    /**
     * 是否下架 0否 1是
     */
    private Integer productFlag;

    private String productName;

    private Double productPrice;

    /**
     * 销量
     */
    private Integer productSales;

    /**
     * 简介
     */

    private String productIntro;

    /**
     * 类型
     */
    private String productType;

    /**
     * 图片
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long imgsId;

    /**
     * 所属商家
     */
    private String storeName;

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

    @TableField(exist = false)
    private List<Author> authorList;

}
