package com.example.bujurui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bujurui.entity.Orders;

/**
 * @author cc
 * @version V1.0
 * @Package com.example.bujurui.service
 * @date 2023/4/30 20:59
 */
public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
