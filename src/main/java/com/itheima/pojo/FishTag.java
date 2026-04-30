package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 鱼标签数据实体类
 */
@Data
public class FishTag {
    private Integer id; // 主键ID
    private int reefNo;
    private long cardNo; // 卡号（十六进制字符串）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime; // 接收时间
}


/*    private double timeout; // 超时时间（秒）
    private int signalStrength; // 信号强度
    private int countryCode; // 国家代号*/