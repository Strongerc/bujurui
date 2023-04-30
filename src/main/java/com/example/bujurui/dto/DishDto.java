package com.example.bujurui.dto;

import com.example.bujurui.entity.Dish;
import com.example.bujurui.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
// 继承自dish 有相应字段
public class DishDto extends Dish {

    // 表没有这个字段
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
