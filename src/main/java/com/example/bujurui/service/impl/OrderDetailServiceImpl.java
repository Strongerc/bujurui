package com.example.bujurui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bujurui.entity.OrderDetail;
import com.example.bujurui.mapper.OrderDetailMapper;
import com.example.bujurui.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author cc
 * @version V1.0
 * @Package com.example.bujurui.service.impl
 * @date 2023/4/30 21:01
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
