package com.itheima.controller;

import com.itheima.pojo.Aquaculture.cages;
import com.itheima.pojo.Aquaculture.eqp;
import com.itheima.pojo.Result;
import com.itheima.service.eqpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eqp")
public class eqpController {
    @Autowired
   private eqpService eqpService;
    @PostMapping("/insert")
    public Result insert(@RequestBody eqp eqp){
        eqpService.insert(eqp);
        return Result.success();
    }

    @GetMapping("/select")
    public Result select(){
        List<eqp> eqp= eqpService.selectData();
        return Result.success(eqp);
    }



    @PutMapping("/update")
    public Result update(@RequestBody eqp eqp){

        if (eqp.getId()==null){
            return Result.error("id不能为空");
        }
        eqpService.update(eqp);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id){
        eqpService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/otherday")
    public Result otherday(@RequestParam String date){
        List<eqp> eqps=eqpService.selectToday(date);
        return Result.success(eqps);
    }
}
