package com.itheima.service;

import com.itheima.pojo.sea.avgSeaData;
import com.itheima.pojo.sea.preSeaData;
import com.itheima.pojo.sea.seaData;

import java.util.List;

public interface seaDataService {
    void insert(seaData seaData);

    List<seaData> selectToday(String date);

    void update(seaData seaData);

    void deleteById(Integer id);

    List<seaData> selectData();

    List<seaData> selectAll();

    List<avgSeaData> selectAvgToday();

    List<preSeaData> selectpreToday();

    List<seaData> selectLatestTen();

    seaData selectLatest();

}
