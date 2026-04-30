package com.itheima.mapper;

import com.itheima.pojo.Aquaculture.cages;
import com.itheima.pojo.Aquaculture.weather;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface weatherMapper {
    @Insert("INSERT INTO weather (wind, rain, fog, inspect_time) \n" +
            "VALUES (#{wind}, #{rain}, #{fog}, #{inspectTime})")
    void insert(weather weather);
    @Select("select * from weather order by inspect_time desc ")
    List<weather> selectData();



    @Update("update weather set wind=#{wind},rain=#{rain},fog=#{fog},inspect_time=#{inspectTime} where id=#{id}")
    void update(weather weather);

    @Delete("delete from weather where id = #{id}")
    void deleteById(Integer id);
    @Select("select * from weather where DATE(inspect_time) = #{date} ")
    List<weather> selectToday(String date);
}
