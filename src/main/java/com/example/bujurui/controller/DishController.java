package com.example.bujurui.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bujurui.common.R;
import com.example.bujurui.dto.DishDto;
import com.example.bujurui.entity.Category;
import com.example.bujurui.entity.Dish;
import com.example.bujurui.entity.DishFlavor;
import com.example.bujurui.service.CategoryService;
import com.example.bujurui.service.DishFlavorService;
import com.example.bujurui.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件 ，没有传name值，就还可以搜索
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝 pageInfo dishDtoPage 本质是一个对象
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        // 取id
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

/**
 * 根据条件查询对应的菜品数据
 * @param dish
 * @return
 */
    // @GetMapping("/list")
    // public R<List<Dish>> list(Dish dish){
    //     //构造查询条件
    //     LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
    //     //添加条件，查询状态为1（起售状态）的菜品
    //     queryWrapper.eq(Dish::getStatus,1);
    //
    //     //添加排序条件
    //     queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    //
    //     List<Dish> list = dishService.list(queryWrapper);
    //
    //     return R.success(list);
    // }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        dishService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    @PostMapping("/status/{staId}")
    public R<String> updateStatus(@PathVariable Integer staId, @RequestParam List<Long> ids) {
        log.info("staId:{} ids:{}", staId, ids);
        List<Dish> dishes = dishService.listByIds(ids);
        if(staId == 0) {
            List<Dish> collect = dishes.stream().map((item) -> {
                item.setStatus(staId);
                dishService.saveOrUpdate(item);
                return item;
            }).collect(Collectors.toList());
        } else {
            List<Dish> collect1 = dishes.stream().map((item) -> {
                item.setStatus(staId);
                dishService.saveOrUpdate(item);
                return item;
            }).collect(Collectors.toList());
        }
        return R.success("状态更新成功");
    }
}
