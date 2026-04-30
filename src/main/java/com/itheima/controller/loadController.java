package com.itheima.controller;

import com.itheima.pojo.Aquaculture.eqp;
import com.itheima.pojo.Result;
import com.itheima.pojo.environmentalLoad;
import com.itheima.pojo.inspectData;
import com.itheima.service.loadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/load")
public class loadController {
    double minRange = 0.0;
    double maxRange = 20.0;
    public static String checkValue(double value, double min, double max) {
        if (value >= min && value <= max) {
            return "正常";
        } else {
            return "报警";
        }
    }
    @Autowired
    private loadService loadService;
    @GetMapping("/selectE")
    public Result selectE(){
          List<environmentalLoad> environmentalLoad= loadService.selectE();

        return Result.success(environmentalLoad);
    }

    @GetMapping("/selectL")
    public Result selectL(){
        List<inspectData>  inspectData=loadService.selectL();
        for (inspectData i:inspectData)
        i.setStatus(checkValue(i.getAcceleration(),minRange,maxRange));
        return Result.success(inspectData);
    }

}
