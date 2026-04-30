package com.itheima.service;

import com.itheima.pojo.environmentalLoad;
import com.itheima.pojo.inspectData;

import java.util.List;

public interface loadService {
    List<environmentalLoad> selectE();

    List<inspectData> selectL();
}
