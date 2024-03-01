package com.ruoyi.system.api.domain;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 农作物种植记录跟踪信息对象 plan_info
 *
 * @author ruoyi
 * @date 2024-02-28
 */
@Data
@ExcelIgnoreUnannotated
public class PlanInfo extends  BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 农作物记录ID
     */
    @ExcelIgnore
    private Long planId;

    /**
     * 农作物信息ID
     */
    @ExcelProperty(index = 0, value = {"农作物种植记录跟踪信息", "农作物信息ID"})
    //@Excel(name = "农作物信息ID")
    private Long cropInfoId;
    /**
     * 记录用户ID
     */
    @ExcelProperty(index = 1, value = {"农作物种植记录跟踪信息", "记录用户ID"})
    //@Excel(name = "记录用户ID")
    private Long userId;

    /**
     * 记录时间
     */
    @ExcelProperty(value = {"农作物种植记录跟踪信息", "记录时间"}, index = 2)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    //@Excel(name = "记录时间", width = 30, dateFormat = "yyyy-MM-dd ")
    private Date recordTime;

    /**
     * 温度
     */
    @ExcelProperty(index = 3,value = {"农作物种植记录跟踪信息", "温度"})
    //@Excel(name = "温度")
    private String temperature;

    /**
     * 生长状况
     */
    @ExcelProperty(index = 4, value = {"农作物种植记录跟踪信息", "生长状况"})
    //@Excel(name = "生长状况")
    private String arowths;

    /**
     * 湿度
     */
    //@Excel(name = "湿度")
    @ExcelProperty(index = 5,value = {"农作物种植记录跟踪信息", "湿度"})
    private String humidness;

    /**
     * 光照
     */
    @ExcelProperty(index = 6,value = {"农作物种植记录跟踪信息", "光照"})
    //@Excel(name = "光照")
    private String illumination;

    /**
     * 农作物图片
     */
    @ExcelProperty(index = 7, value = {"农作物种植记录跟踪信息", "农作物图片"})
    //@Excel(name = "农作物图片")
    private String pic;

    /**
     * 逻辑删除(0正常  1删除)
     *
     * @param planId
     */
    private String delFlag;

    /**
     * 记录人姓名
     *
     * @param planId
     */
    @ExcelProperty(index = 8, value = {"农作物种植记录跟踪信息", "记录人姓名"})
    private String recordName;
    /**
     * 农产品记录编号
     *
     * @param planId
     */
    @ExcelProperty(index = 9, value = {"农作物种植记录跟踪信息", "记录编号"})
    private Long planNum;
    /**
     * 记录用户对象
     *
     * @param planId
     */
    private SysUser user;
    /**
     * 备注
     * @param planId
     */
    @ExcelProperty(index = 10, value = {"农作物种植记录跟踪信息", "备注"})
    private String remark;
    /**
     * 农作物关联对象
     */
    private CropInfo cropInfo;


}
