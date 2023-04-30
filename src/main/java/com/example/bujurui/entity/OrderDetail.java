package com.example.bujurui.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author cc
 * @version V1.0
 * @Package com.example.bujurui.entity
 * @date 2023/4/30 20:58
 */

/**
 * 订单明细
 */
@Data
public class OrderDetail {

    private Long id;

    //名称
    private String name;

    //订单id
    private Long orderId;


    //菜品id
    private Long dishId;


    //套餐id
    private Long setmealId;


    //口味
    private String dishFlavor;


    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //图片
    private String image;
}
