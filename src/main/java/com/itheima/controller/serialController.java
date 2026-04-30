package com.itheima.controller;

import com.itheima.pojo.FishTag;
import com.itheima.pojo.Result;
import com.itheima.pojo.sea.seaData;
import com.itheima.service.FishTagService;
import com.itheima.service.SerialReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/serial")
public class serialController {
    @Autowired
    private SerialReceiveService serialService;
@Autowired
private FishTagService fishTagService;

    @GetMapping("/latest")
    public Result getLatestData() {
        return Result.success(serialService.getLatestHdxData());
    }

    @GetMapping("/getAll")
    public Result echo(){
        List<FishTag> seaData= fishTagService.getAllFishTags();
        return Result.success(seaData);
    }
}
