package com.ruoyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.api.domain.CropInfo;

import java.util.List;

/**
 * 农作物信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-28
 */
public interface CropInfoMapper  extends BaseMapper<CropInfo>
{
    /**
     * 查询农作物信息
     * 
     * @param id 农作物信息主键
     * @return 农作物信息
     */
    public CropInfo selectCropInfoById(Long id);

    /**
     * 查询农作物信息列表
     * 
     * @param cropInfo 农作物信息
     * @return 农作物信息集合
     */
    public List<CropInfo> selectCropInfoList(CropInfo cropInfo);

    /**
     * 新增农作物信息
     * 
     * @param cropInfo 农作物信息
     * @return 结果
     */
    public int insertCropInfo(CropInfo cropInfo);

    /**
     * 修改农作物信息
     * 
     * @param cropInfo 农作物信息
     * @return 结果
     */
    public int updateCropInfo(CropInfo cropInfo);

    /**
     * 删除农作物信息
     * 
     * @param id 农作物信息主键
     * @return 结果
     */
    public int deleteCropInfoById(Long id);

    /**
     * 批量删除农作物信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCropInfoByIds(Long[] ids);
}
