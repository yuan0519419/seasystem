package com.itheima.service;

import com.itheima.pojo.Aquaculture.cages;
import com.itheima.pojo.sea.seaData;

import java.util.List;

public interface cageService {
    void insert(cages cage);

    List<cages> selectData();

    void update(cages cages);

    void deleteById(Integer id);

    List<cages> selectToday(String date);
}
