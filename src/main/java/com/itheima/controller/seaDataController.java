package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.pojo.sea.avgSeaData;
import com.itheima.pojo.sea.preSeaData;
import com.itheima.pojo.sea.seaData;
import com.itheima.service.seaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seaData")
public class seaDataController {
    @Autowired
    private seaDataService seaDataService;
    @PostMapping("/insert")
    public Result insert(@RequestBody seaData seaData){
        seaDataService.insert(seaData);
        return Result.success();
    }


    @GetMapping("/today")
    public Result today(){
        List<seaData> seaData= seaDataService.selectData();
        return Result.success(seaData);
    }


    @GetMapping("/echo")
    public Result echo(){
        List<seaData> seaData= seaDataService.selectAll();
        return Result.success(seaData);
    }

    @GetMapping("/otherday")
    public Result otherday(@RequestParam String date){
        List<seaData> seaData=seaDataService.selectToday(date);
        return Result.success(seaData);
    }

    @PutMapping("/update")
    public Result update(@RequestBody   seaData seaData){

        if (seaData.getId()==null){
            return Result.error("id不能为空");
        }
        seaDataService.update(seaData);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id){
         seaDataService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/avgToday")
    public Result avgToday(){
        List<avgSeaData> avgSeaDataList= seaDataService.selectAvgToday();
        return Result.success(avgSeaDataList);
    }


    @GetMapping("/preToday")
    public Result preToday(){
        List<preSeaData> preSeaDataList= seaDataService.selectpreToday();
        return Result.success(preSeaDataList);
    }

    @GetMapping("/latestTen")
    public Result latestTen(){
        List<seaData> seaDataList= seaDataService.selectLatestTen();
        return Result.success(seaDataList);
    }

    @GetMapping("/latest")
    public Result latest(){
        seaData seaData= seaDataService.selectLatest();
        return Result.success(seaData);
    }
}
