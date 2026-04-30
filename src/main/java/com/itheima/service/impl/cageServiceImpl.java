package com.itheima.service.impl;

import com.itheima.mapper.cageMapper;
import com.itheima.pojo.Aquaculture.cages;
import com.itheima.pojo.sea.seaData;
import com.itheima.service.cageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class cageServiceImpl implements cageService {
    @Autowired
    private cageMapper cageMapper;
    @Override
    public void insert(cages cage) {
        cageMapper.insert(cage);
    }

    @Override
    public List<cages> selectData() {
        List<cages> cages=cageMapper.selectData();
        return cages;
    }

    @Override
    public void update(cages cages) {
        cageMapper.update(cages);
    }

    @Override
    public void deleteById(Integer id) {
        cageMapper.deleteById(id);
    }

    @Override
    public List<cages> selectToday(String date) {
        List<cages> cages = cageMapper.selectToday(date);
        System.out.println(
                cages.toString()
        );
        return cages;
    }
}
