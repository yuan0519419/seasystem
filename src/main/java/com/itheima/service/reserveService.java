package com.itheima.service;

import com.itheima.pojo.Aquaculture.reserve;

import java.util.List;

public interface reserveService {
    void insert(reserve reserve);

    List<reserve> selectData();

    void update(reserve reserve);

    void deleteById(Integer id);

    List<reserve> selectToday(String date);
}
