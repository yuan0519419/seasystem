package com.itheima.controller;


import com.itheima.pojo.Aquaculture.reserve;
import com.itheima.pojo.Aquaculture.weather;
import com.itheima.pojo.Result;
import com.itheima.service.weatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class weatherController {
    @Autowired
    private weatherService weatherService;
    @PostMapping("/insert")
    public Result insert(@RequestBody weather weather){
        weatherService.insert(weather);
        return Result.success();
    }

    @GetMapping("/select")
    public Result select(){
        List<weather> weathers= weatherService.selectData();
        return Result.success(weathers);
    }



    @PutMapping("/update")
    public Result update(@RequestBody weather weather){

        if (weather.getId()==null){
            return Result.error("id不能为空");
        }
        weatherService.update(weather);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id){
        weatherService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/otherday")
    public Result otherday(@RequestParam String date){
        List<weather> weather=weatherService.selectToday(date);
        return Result.success(weather);
    }
}
