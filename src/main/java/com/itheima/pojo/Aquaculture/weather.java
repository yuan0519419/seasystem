package com.itheima.pojo.Aquaculture;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class weather {
    /** 自增主键ID */
    private Integer id;

    /** 风场描述 */
    private String wind;

    /** 降水量(mm) */
    private double rain;

    /** 海雾情况 */
    private String fog;
    /** 数据抽检时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inspectTime;
}
