package com.ruoyi.system.api.domain;

import com.alibaba.excel.annotation.ExcelIgnore;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;


/**
 * 农作物信息表
 *
 * @TableName crop_info
 */
@Data
public class CropInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private Integer id;

    /**
     * 编号
     */
    @Excel(name = "cropNum")
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
    @Excel(name = "cropName")
    private String cropName;

    /**
     * 登记时间
     */
    @Excel(name = "registrationTime")
    private Date registrationTime;


    /**
     * 周期
     */
    @Excel(name = "cycle")
    private String cycle;

    /**
     * 负责人
     */
    @Excel(name = "baseCharge")
    private String baseCharge;

    /**
     * 负责人联系方式
     */
    @Excel(name = "basePhone")
    private String basePhone;

    /**
     * 种植方式id
     */

    private Integer planmethodId;

    /**
     * 灌溉方式id
     */

    private Integer irrigationId;
    /*
    报表灌溉名称
     */

    private String irr;
     /*
     报表灌溉数量
      */
    private Integer irrnum;
    /**
     * 肥料类型id
     */

    private Integer fertId;

    /**
     * 备注
     */
    private String remark;
    /**
     * 灌溉方式
     */
    @Excel(name = "planName")
    private String planName;
    /**
     * 肥料类型
     */
    @Excel(name = "fertName")
    private String fertName;
    /**
     * 种植方式
     */
    @Excel(name = "irrName")
    private String irriName;

}