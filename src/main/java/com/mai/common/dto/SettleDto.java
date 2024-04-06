package com.mai.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SettleDto implements Serializable {
    private Double orderPrice;
    private String orderAddress;
    private String userId;
    private String storeName;
    private Long productId;
    private String productName;

    // 为了购买数量所以设计的这个dto
    private Integer num;
}
