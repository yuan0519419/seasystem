package com.itheima.service;

import com.itheima.pojo.FishTag;

import java.util.List;

public interface SerialReceiveService {
    void startListening();
    FishTag parseHdxData(byte[] data);

    FishTag getLatestHdxData();


}
