package com.itheima.controller;

import com.itheima.pojo.Aquaculture.eqp;
import com.itheima.pojo.Aquaculture.reserve;
import com.itheima.pojo.Result;
import com.itheima.service.reserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserve")
public class reserveController {
    @Autowired
    private reserveService reserveService;
    @PostMapping("/insert")
    public Result insert(@RequestBody reserve reserve){
        reserveService.insert(reserve);
        return Result.success();
    }

    @GetMapping("/select")
    public Result select(){
        List<reserve> reserves= reserveService.selectData();
        return Result.success(reserves);
    }



    @PutMapping("/update")
    public Result update(@RequestBody reserve reserve){

        if (reserve.getId()==null){
            return Result.error("id不能为空");
        }
        reserveService.update(reserve);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id){
        reserveService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/otherday")
    public Result otherday(@RequestParam String date){
        List<reserve> reserve=reserveService.selectToday(date);
        return Result.success(reserve);
    }
}
