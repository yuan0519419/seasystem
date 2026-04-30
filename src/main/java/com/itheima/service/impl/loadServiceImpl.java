package com.itheima.service.impl;

import com.itheima.mapper.loadMapper;
import com.itheima.pojo.environmentalLoad;
import com.itheima.pojo.inspectData;
import com.itheima.service.loadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class loadServiceImpl implements loadService {
    @Autowired
    private loadMapper loadMapper;
    @Override
    public List<environmentalLoad> selectE() {
        return loadMapper.selectE();
    }

    @Override
    public List<inspectData> selectL() {
        return loadMapper.selectL();
    }
}
