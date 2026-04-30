package com.itheima.pojo.Aquaculture;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
@Data
public class eqp {
    private Integer id;

    /** 结构件状况 */

    private String strucStat;

    /** 网衣清洗状况 */

    private String netStat;

    /** 抽检时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sTime;


}
