package com.mai.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`order`") // 因为和数据库关键字冲突（如果是字段与关键字冲突则主键@TableId 其它@TableField）
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "order_id", type = IdType.ASSIGN_ID) // 3.3.0才有雪花算法……
    private Long orderId;

    private Double orderPrice;

    /**
     * 0未发货 1已发货 2已签收 3已评论
     */
    private Integer orderStatus;

    /**
     * 该订单对应地址信息
     */
    private String orderAddress;

    private String userId;

    private String storeName;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    private String productName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;


}
