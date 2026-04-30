package com.itheima.service.impl;

import com.itheima.mapper.eqpMapper;
import com.itheima.pojo.Aquaculture.cages;
import com.itheima.pojo.Aquaculture.eqp;
import com.itheima.service.eqpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class eqpServiceImpl implements eqpService {
    @Autowired
    private eqpMapper eqpMapper;
    @Override
    public void insert(eqp eqp) {
        eqpMapper.insert(eqp);
    }

    @Override
    public List<eqp> selectData() {
        List<eqp> eqp=eqpMapper.selectData();
        return eqp;
    }

    @Override
    public void update(eqp eqp) {
        eqpMapper.update(eqp);
    }

    @Override
    public void deleteById(Integer id) {
        eqpMapper.deleteById(id);
    }

    @Override
    public List<eqp> selectToday(String date) {
        return eqpMapper.selectToday(date);
    }
}
