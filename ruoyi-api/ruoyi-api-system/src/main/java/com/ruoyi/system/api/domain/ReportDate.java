package com.ruoyi.system.api.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author 卟哭！
 * @Date 2024/03/02  10:44
 * @Version 1.0
 */
@Data
public class ReportDate implements Serializable {
    /**
     * 报表数据 X Y轴
     */
    //名称
    private String x;
    //数量
    private String y;
//    名称
    private String name;
//    数量
    private String value;
}
