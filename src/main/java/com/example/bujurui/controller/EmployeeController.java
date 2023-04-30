package com.example.bujurui.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bujurui.common.R;
import com.example.bujurui.entity.Employee;
import com.example.bujurui.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author cc
 * @version V1.0
 * @Package com.example.bujurui.controller
 * @date 2023/4/8 20:09
 */


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /*
     * 导入employeeService，进行信息查找
     * @RequestMapping("/employee") 员工需要请求，谁需要请求
     * @PostMapping("/login") 员工需要登录，并且携带请求信息，获取：@RequestBody：从请求体中获取携带的信息
     * HttpServletRequest request 将查到的对象放入session。方便展示员工在平台信息，而不必每次都先后台申请
     * Session未被访问,开始计时， Session的默认失效时间是30分钟
     * 本次登录逻辑为  md5加密-->根据用户名查询数据库-->比对密码是否一致-->查看员工状态是否被锁
     * else ： 返回登录失败结果-->返回登录失败结果-->返回员工状态禁用结果
     * else：  将员工Id放入session，返回成功结果
     * String password = employee.getPassword(); 对密码进行加密的时候先申明一个变量接受
     * queryWrapper使用时，可以结合employeeService条件查询
     * IService下有getOne()用于条件查询，根据姓名，id，年龄，、、、,也可以多个条件进行限制
     *   注意：有可能同个姓名查询，会出现同名现象，这个时候，可以对字段进行限制，既唯一约束
     * 找了信息，判断找到没有，没有返回消息提示，单条件，
     * if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }，做条件判断后，每一个都可以进行方法，就不需要if-else了
        * 比对密码 A.equal(B) 结合if，不需要申明变量
        *
        * if(true|false) return  R
        * 前台可以根据id，预定义相关信息
        * 可以将查询到的所有信息全部返回，免于前端的工作量
        * 关于测试 打上断点，然后单步执行，最后放行，完美的测试，是对应所有if判断
        *
     * */



    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */

    // 由于session在登录时存了相应id，表示已登录，在注销时，要移除相应id
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /*
    *  R<String>，不需要数据返回，因此传简单类型
    *
    *
    * */


    /**
     * 新增员工
     * @param employee
     * @return
     */

    /*
    *
    *
    *
    *
    * */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     *
     * LIMIT ? 用于分页
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }

}
