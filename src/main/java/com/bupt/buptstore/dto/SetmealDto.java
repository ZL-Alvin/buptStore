package com.bupt.buptstore.dto;


import com.bupt.buptstore.pojo.Setmeal;
import com.bupt.buptstore.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
