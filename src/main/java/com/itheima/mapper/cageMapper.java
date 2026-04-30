package com.itheima.mapper;

import com.itheima.pojo.Aquaculture.cages;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface cageMapper {
    @Insert("INSERT INTO cages (loc, size, breed, place_date)\n" +
            "VALUES (#{loc}, #{size}, #{breed}, #{placeDate})")
    void insert(cages cage);
@Select("select * from cages order by place_date desc")
    List<cages> selectData();



@Update("update cages set loc=#{loc},size=#{size},breed=#{breed},place_date=#{placeDate} where id=#{id}")
    void update(cages cages);

@Delete("delete from cages where id = #{id}")
    void deleteById(Integer id);
@Select("select * from cages where DATE(place_date) = #{date} ")
    List<cages> selectToday(String date);
}
