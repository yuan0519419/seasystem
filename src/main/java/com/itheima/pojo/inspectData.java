package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class inspectData {
    private  Integer id;
    private  Integer x ;
    private  Integer y;
    private  double acceleration;//加速度范围
    private String status;//状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordedAt; //数据记录时间(精确到小时)
}
