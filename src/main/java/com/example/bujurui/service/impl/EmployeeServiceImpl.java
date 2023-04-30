package com.example.bujurui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bujurui.entity.Employee;
import com.example.bujurui.mapper.EmployeeMapper;
import com.example.bujurui.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author cc
 * @version V1.0
 * @Package com.example.bujurui.service.impl
 * @date 2023/4/8 20:06
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
