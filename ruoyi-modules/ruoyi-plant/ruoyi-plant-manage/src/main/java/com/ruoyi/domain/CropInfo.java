package com.ruoyi.domain;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.ruoyi.common.core.annotation.Excel;
import feign.Experimental;
import lombok.Data;

import javax.naming.Name;

/**
 * 农作物信息表
 * @TableName crop_info
 */
@TableName(value ="crop_info")
@Data
public class CropInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 编号  
     */
    @ExcelIgnore
    private Long cropNum;
    /**
     * 状态(软删除)
     */
    private Integer status;

    /**
     * 种植基地id
     */
    private Integer baseId;

    /**
     * 农作物名称
     */

    private String cropName;

    /**
     * 登记时间
     */

    private Date registrationTime;

    @TableField(exist = false)
    private String time;

    public String getTime(){
         return DateUtil.format(this.getRegistrationTime(),"yyyy-MM-dd");
    }

    /**
     * 周期
     */

    private String cycle;

    /**
     * 负责人
     */

    private String baseCharge;

    /**
     * 负责人联系方式
     */

    private String basePhone;

    /**
     * 种植方式id
     */

    private Integer planmethodId;

    /**
     * 灌溉方式id
     */

    private Integer irrigationId;

    /**
     * 肥料类型id
     */

    private Integer fertId;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private String planName;

    @TableField(exist = false)
    private String fertName;

    @TableField(exist = false)
    private String irriName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}