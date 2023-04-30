package com.example.bujurui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bujurui.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cc
 * @version V1.0
 * @Package com.example.bujurui.mapper
 * @date 2023/4/30 21:01
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}
