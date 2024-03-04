package com.ruoyi.system.api.domain;

import com.alibaba.excel.annotation.ExcelIgnore;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registrationTime;


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
    /**
     * 灌溉方式
     */
    private String planName;
    /**
     * 肥料类型
     */
    private String fertName;
    /**
     * 种植方式
     */
    private String irriName;

}