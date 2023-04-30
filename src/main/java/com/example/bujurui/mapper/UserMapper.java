package com.example.bujurui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bujurui.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
