package com.example.bujurui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bujurui.entity.User;
import com.example.bujurui.mapper.UserMapper;
import com.example.bujurui.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
