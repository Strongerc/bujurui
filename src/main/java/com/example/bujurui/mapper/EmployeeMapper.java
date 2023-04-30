package com.example.bujurui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bujurui.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
