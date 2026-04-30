package com.itheima.mapper;

import com.itheima.pojo.environmentalLoad;
import com.itheima.pojo.inspectData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface loadMapper {
@Select("select * from environmental_loads order by recorded_at desc limit 1")
    List<environmentalLoad> selectE();
@Select("select * from inspection_data order by recorded_at desc")
    List<inspectData> selectL();
}
