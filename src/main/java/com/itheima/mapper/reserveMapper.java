package com.itheima.mapper;

import com.itheima.pojo.Aquaculture.cages;
import com.itheima.pojo.Aquaculture.reserve;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface reserveMapper {
    @Insert("INSERT INTO reserve (feed, net, inspect_time)\n" +
            "VALUES (#{feed}, #{net}, #{inspectTime})")
    void insert(reserve reserve);
    @Select("select * from reserve order by inspect_time desc")
    List<reserve> selectData();



    @Update("update reserve set feed=#{feed},net=#{net},inspect_time=#{inspectTime} where id=#{id}")
    void update(reserve reserve);

    @Delete("delete from reserve where id = #{id}")
    void deleteById(Integer id);
    @Select("select * from reserve where DATE(inspect_time) = #{date} ")
    List<reserve> selectToday(String date);
}
