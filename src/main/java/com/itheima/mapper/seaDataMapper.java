package com.itheima.mapper;

import com.itheima.pojo.sea.avgSeaData;
import com.itheima.pojo.sea.preSeaData;
import com.itheima.pojo.sea.seaData;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface seaDataMapper {
    @Insert("insert into seadata (o, nh, no, temp, salinity, turbidity, ph, chl, curr_vel,orp,sample_time)\n" +
            "values\n" +
            "(#{o}, #{nh},#{no}, #{temp}, #{salinity}, #{turbidity}, #{ph},#{chl}, #{currVel},#{orp},#{sampleTime})")
    void insert(seaData seaData);

@Select("select * from seadata where DATE(sample_time) = #{date} order by sample_time ASC")
    List<seaData> selectToday(String date);
@Update("update seadata set o=#{o}, nh=#{nh}, no=#{no}, temp=#{temp}," +
        " salinity=#{salinity}, turbidity= #{turbidity}, ph=#{ph}, chl=#{chl}, curr_vel=#{currVel} where id=#{id}")
    void update(seaData seaData);
@Delete("delete from seadata where id = #{id};")
    void deleteById(Integer id);
/*@Select("SELECT * FROM seaData\n" +
        "WHERE (sample_time >= CURDATE() AND sample_time < CURDATE() + INTERVAL 1 DAY)\n" +
        "   OR (sample_time >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) AND sample_time < CURDATE())\n" +
        "ORDER BY sample_time DESC\n" +
        "LIMIT 5;\n")*/


//查询近两天最新的24条数据
  /* @Select("SELECT * FROM seaData\n" +
           "WHERE (sample_time >= CURDATE() AND sample_time < CURDATE() + INTERVAL 1 DAY)\n" +
           "   OR (sample_time >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) AND sample_time < CURDATE())\n" +
           "ORDER BY sample_time DESC\n" +
           "LIMIT 5;")*/
@Select("select * from seadata order by sample_time desc limit 5")
    List<seaData> selactData();

    @Select("select * from seadata order by sample_time desc limit 10")
    List<seaData> selectLatestTen();

    @Select("select * from seadata order by sample_time desc limit 1")
    seaData selectLatest();


   @Select("select * from seadata ORDER BY sample_time DESC")
    List<seaData> selectAll();



    @Select("SELECT sample_time from seadata where id=#{id}")
    LocalDateTime selectDataByid(Integer id);






    @Select("SELECT * FROM avg_seadata ORDER BY day DESC LIMIT 10;")
    List<avgSeaData> selectAvgToday();
@Select("SELECT\n" +
        "    ROUND(AVG(o), 2) AS o,              -- 平均溶解氧，保留两位小数\n" +
        "    ROUND(AVG(nh), 2) AS nh,            -- 平均氨氮\n" +
        "    ROUND(AVG(no), 2) AS no,            -- 平均亚硝酸氮\n" +
        "    ROUND(AVG(temp), 2) AS temp,      -- 平均水温\n" +
        "    ROUND(AVG(salinity), 2) AS salinity,     -- 平均盐度\n" +
        "    ROUND(AVG(turbidity), 2) AS turbidity,   -- 平均浊度\n" +
        "    ROUND(AVG(ph), 2) AS ph,                 -- 平均pH值\n" +
        "    ROUND(AVG(chl), 2) AS chl,       -- 平均叶绿素a\n" +
        "    ROUND(AVG(curr_vel), 2) AS curr_vel, -- 平均洋流流速\n" +
        "    ROUND(AVG(orp), 2) AS orp                -- 平均氧化还原电位（整数四舍五入后仍为整数）\n" +
        "FROM seadata WHERE DATE(sample_time) = #{date}")
    avgSeaData selectAvg(LocalDate date);
@Update("update avg_seadata set o=#{o}, nh=#{nh}, no=#{no}, temp=#{temp}," +
        "salinity=#{salinity}, turbidity= #{turbidity}, ph=#{ph}, chl=#{chl}," +
        " curr_vel=#{currVel} ,orp=#{orp} where day = #{day}")
    void updateAvg(avgSeaData avgSeaData);
@Select("SELECT COUNT(*) FROM avg_seadata WHERE day = #{date};")
    int count(LocalDate date);
    @Insert("insert into avg_seadata (o, nh, no, temp, salinity, turbidity, ph, chl, curr_vel,orp,day)\n" +
            "values\n" +
            "(#{o}, #{nh},#{no}, #{temp}, #{salinity}, #{turbidity}, #{ph},#{chl}, #{currVel},#{orp},#{day})")
    void insertAvg(avgSeaData avgSeaData);
@Select("select * from preseadata ORDER BY sample_time DESC")
    List<preSeaData> selectPreToday();

}
