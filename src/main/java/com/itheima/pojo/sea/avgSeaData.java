package com.itheima.pojo.sea;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class avgSeaData {
    private  Integer id;
    private  double o; // '溶解氧(mg/L)'
    private  double nh ;// '氨氮(mg/L)',
    private  double no; //'亚硝酸氮(mg/L)',
    private double temp ;//'水温(℃)',
    private double salinity; // '盐度(PSU)',
    private  double turbidity ;//'浊度(NTU)',
    private double ph ;// 'pH值',
    private  double chl;// '叶绿素a(μg/L)',
    private  double currVel;// '洋流流速(cm/s)'
    private Integer orp;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;
}
