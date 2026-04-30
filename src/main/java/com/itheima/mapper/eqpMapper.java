package com.itheima.mapper;

import com.itheima.pojo.Aquaculture.eqp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface eqpMapper {
    @Insert("insert into eqp (struc_stat, net_stat, s_time) values (#{strucStat},#{netStat},#{sTime})")
    void insert(eqp eqp);
@Select("select * from eqp order by s_time desc ")
    List<eqp> selectData();
@Update("update eqp set struc_stat=#{strucStat},net_stat=#{netStat},s_time=#{sTime} where id=#{id}")
    void update(eqp eqp);
@Delete("delete from eqp where id =#{id}")
    void deleteById(Integer id);
    @Select("select * from eqp where DATE(s_time) = #{date} ")
    List<eqp> selectToday(String date);
}
