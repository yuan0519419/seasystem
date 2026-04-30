package com.itheima.service.impl;

import com.itheima.mapper.seaDataMapper;
import com.itheima.pojo.sea.avgSeaData;
import com.itheima.pojo.sea.preSeaData;
import com.itheima.pojo.sea.seaData;
import com.itheima.service.seaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class seaDataServiceImpl implements seaDataService {
    @Autowired
    private seaDataMapper seaDataMapper;
    @Override
    public void insert(seaData seaData) {

        seaDataMapper.insert(seaData);


        LocalDateTime dateTime = seaData.getSampleTime();
        // 获取日期部分（年、月、日）
        LocalDate date = dateTime.toLocalDate();
        //获取品均数
        avgSeaData avgSeaData=seaDataMapper.selectAvg(date);
        //封装对象
        avgSeaData.setDay(date);
        //判断表中是否存在
        int count=seaDataMapper.count(date);
        if(count>0){
            seaDataMapper.updateAvg(avgSeaData);
        }
        if(count==0){
            seaDataMapper.insertAvg(avgSeaData);
        }






    }

    @Override
    public List<seaData> selectToday(String date) {
        List<seaData> seaData=seaDataMapper.selectToday(date);
        return seaData;
    }

    @Override
    public void update(seaData seaData) {
        seaDataMapper.update(seaData);

        LocalDateTime dateTime = seaData.getSampleTime();
        // 获取日期部分（年、月、日）
        LocalDate date = dateTime.toLocalDate();
        //获取品均数
        avgSeaData avgSeaData=seaDataMapper.selectAvg(date);

        //判断表中是否存在
        int count=seaDataMapper.count(date);
        if(count>0){
            seaDataMapper.updateAvg(avgSeaData);
        }
        if(count==0){
            seaDataMapper.insertAvg(avgSeaData);
        }


    }

    @Override
    public void deleteById(Integer id) {
        seaDataMapper.deleteById(id);//删除
        LocalDateTime dateTime=seaDataMapper.selectDataByid(id);//获取该id的日期
        LocalDate date = dateTime.toLocalDate();
        avgSeaData avgSeaData=seaDataMapper.selectAvg(date);//获取该天的平均值
        //更新avg表的数据
        //判断表中是否存在
        int count=seaDataMapper.count(date);
        if(count>0){
            seaDataMapper.updateAvg(avgSeaData);
        }
        if(count==0){
            seaDataMapper.insertAvg(avgSeaData);
        }
    }

    @Override
    public List<seaData> selectData() {
        List<seaData> seaData=seaDataMapper.selactData();
        return seaData;
    }

    @Override
    public List<seaData> selectAll() {
        return  seaDataMapper.selectAll();
    }

    @Override
    public List<avgSeaData> selectAvgToday() {
        return seaDataMapper.selectAvgToday();
    }

    @Override
    public List<preSeaData> selectpreToday() {
        return seaDataMapper.selectPreToday();
    }

    @Override
    public List<seaData> selectLatestTen() {
        return seaDataMapper.selectLatestTen();
    }

    @Override
    public seaData selectLatest() {
        return seaDataMapper.selectLatest();
    }

}
