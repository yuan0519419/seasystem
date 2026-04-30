package com.itheima.controller;

import com.itheima.pojo.Aquaculture.cages;
import com.itheima.pojo.Result;
import com.itheima.pojo.sea.seaData;
import com.itheima.service.cageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cages")
public class cagesController {
    @Autowired
    private cageService cageService;
    @PostMapping("/insert")
    public Result insert(@RequestBody cages cage){
        cageService.insert(cage);
        return Result.success();
    }


    @GetMapping("/select")
    public Result select(){
        List<cages> cages= cageService.selectData();
        return Result.success(cages);
    }



    @PutMapping("/update")
    public Result update(@RequestBody cages cages){

        if (cages.getId()==null){
            return Result.error("id不能为空");
        }
        cageService.update(cages);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id){
        cageService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/otherday")
    public Result otherday(@RequestParam String date){
        List<cages> cages=cageService.selectToday(date);
        return Result.success(cages);
    }
}
