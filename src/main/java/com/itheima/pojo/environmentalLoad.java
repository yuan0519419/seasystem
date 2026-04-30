package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class environmentalLoad {
    private  Integer id; // 自增主键
    private Integer windLevel ;// 风力等级(级)
    private Integer windLoad ;//正面风载荷(KN)
    private  Integer currentSpeed ;// 流载荷(m/s)
    private Integer netLoad ;//网衣流载荷(KN)
    private Integer waveHeight; //波高(m)
    private  Integer verticalFrameLoad;// 平台垂直框架载荷(KN)
    private Integer horizontalFrameLoad;//平台水平框架载荷(KN)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordedAt ;//数据记录时间(精确到小时)
}
