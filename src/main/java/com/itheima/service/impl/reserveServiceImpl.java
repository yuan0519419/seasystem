package com.itheima.service.impl;

import com.itheima.mapper.reserveMapper;
import com.itheima.pojo.Aquaculture.reserve;
import com.itheima.service.reserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class reserveServiceImpl implements reserveService {
    @Autowired
    private reserveMapper reserveMapper;
    @Override
    public void insert(reserve reserve) {
    reserveMapper.insert(reserve);
    }

    @Override
    public List<reserve> selectData() {
        List<reserve> reserves=reserveMapper.selectData();
        return reserves;
    }

    @Override
    public void update(reserve reserve) {
     reserveMapper.update(reserve);
    }

    @Override
    public void deleteById(Integer id) {
    reserveMapper.deleteById(id);
    }

    @Override
    public List<reserve> selectToday(String date) {
        return reserveMapper.selectToday(date);
    }
}
