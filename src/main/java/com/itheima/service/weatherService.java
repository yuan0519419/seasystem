package com.itheima.service;

import com.itheima.pojo.Aquaculture.weather;

import java.util.List;

public interface weatherService {
    void insert(weather weather);

    List<weather> selectData();

    void update(weather weather);

    void deleteById(Integer id);

    List<weather> selectToday(String date);
}
