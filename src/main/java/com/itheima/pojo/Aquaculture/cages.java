package com.itheima.pojo.Aquaculture;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class cages {
    // 网箱唯一标识ID
    private Integer id;
    // 网箱所在地理位置
    private String loc;
    // 网箱规格(容量m3)
    private Integer size;
    // 养殖品种名称
    private String breed;
    @JsonFormat(pattern = "yyyy-MM-dd")
    // 鱼苗投放日期
    private LocalDate placeDate;



}

