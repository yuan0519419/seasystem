package com.itheima.pojo.Aquaculture;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class reserve {
    private Integer id;
    //饲料数量(包)
    private Integer feed;
    //网衣数量(件)
    private Integer net;
    //抽检时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inspectTime;
}
