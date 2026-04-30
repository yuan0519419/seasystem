package com.itheima.service.impl;

import com.itheima.mapper.FishTagMapper;
import com.itheima.pojo.FishTag;
import com.itheima.service.FishTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishTagServiceImpl implements FishTagService {

    @Autowired
    private FishTagMapper fishTagMapper;

    @Override
    public void saveFishTag(FishTag fishTag) {
        fishTagMapper.save(fishTag);
    }


    @Override
    public List<FishTag> getAllFishTags() {
        return fishTagMapper.findAll();
    }

}
