package com.itheima.pojo.sea;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class preSeaData {

    private  Integer id;
    private  double o; // '溶解氧(mg/L)'
    private  double nh ;// '氨氮(mg/L)',
    private  double no; //'亚硝酸氮(mg/L)',
    private double temp ;//'水温(℃)',
    private double salinity; // '盐度(PSU)',
    private  double turbidity ;//'浊度(NTU)',
    private double ph ;// 'pH值',
    private  double chl;// '叶绿素a(μg/L)',
    private  double currVel;// '洋流流速(cm/s)',
    private Integer orp;//氧化还原电位
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sampleTime;// '采样时间'

}
