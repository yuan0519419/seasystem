package com.itheima.mapper;

import com.itheima.pojo.FishTag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FishTagMapper {
    // 保存鱼标签数据
    @Insert("insert into fish_tag(reef_no, card_no, receive_time) values(#{reefNo}, #{cardNo},#{receiveTime})")
    void save(FishTag fishTag);

    // 获取所有鱼标签数据
    @Select("select * from fish_tag order by receive_time desc")
    List<FishTag> findAll();


}
