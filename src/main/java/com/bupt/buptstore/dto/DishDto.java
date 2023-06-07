package com.bupt.buptstore.dto;


import com.bupt.buptstore.pojo.Dish;
import com.bupt.buptstore.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
