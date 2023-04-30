package com.example.bujurui.controller;

/**
 * @author cc
 * @version V1.0
 * @Package com.example.bujurui.controller
 * @date 2023/4/30 21:02
 */

import com.example.bujurui.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单明细
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;
}
