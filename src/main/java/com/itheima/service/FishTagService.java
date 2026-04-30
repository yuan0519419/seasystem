package com.itheima.service;

import com.itheima.pojo.FishTag;

import java.util.List;

public interface FishTagService {

    void saveFishTag(FishTag fishTag);

    List<FishTag> getAllFishTags();


}
