package com.itheima.service;

import com.itheima.pojo.Aquaculture.eqp;

import java.util.List;

public interface eqpService {
    void insert(eqp eqp);

    List<eqp> selectData();

    void update(eqp eqp);

    void deleteById(Integer id);

    List<eqp> selectToday(String date);
}
