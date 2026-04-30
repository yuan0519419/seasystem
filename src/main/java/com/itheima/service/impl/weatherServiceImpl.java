package com.itheima.service.impl;

import com.itheima.mapper.weatherMapper;
import com.itheima.pojo.Aquaculture.weather;
import com.itheima.service.weatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class weatherServiceImpl implements weatherService {
    @Autowired
    private weatherMapper weatherMapper;
    @Override
    public void insert(weather weather) {
    weatherMapper.insert(weather);
    }

    @Override
    public List<weather> selectData() {
        List<weather> weathers = weatherMapper.selectData();
        return weathers;
    }

    @Override
    public void update(weather weather) {
    weatherMapper.update(weather);
    }

    @Override
    public void deleteById(Integer id) {
    weatherMapper.deleteById(id);
    }

    @Override
    public List<weather> selectToday(String date) {
        return weatherMapper.selectToday(date);
    }
}
