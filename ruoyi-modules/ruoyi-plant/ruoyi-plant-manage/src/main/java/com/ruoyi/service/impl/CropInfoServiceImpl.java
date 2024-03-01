package com.ruoyi.service.impl;


import java.util.List;


import com.ruoyi.system.api.domain.CropInfo;

import com.ruoyi.mapper.CropInfoMapper;
import com.ruoyi.service.ICropInfoService;
import com.ruoyi.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 农作物信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-02-28
 */
@Service
public class CropInfoServiceImpl implements ICropInfoService
{
    @Autowired
    private CropInfoMapper cropInfoMapper;

    /**
     * 查询农作物信息
     * 
     * @param id 农作物信息主键
     * @return 农作物信息
     */
    @Override
    public CropInfo selectCropInfoById(Long id)
    {
        return cropInfoMapper.selectCropInfoById(id);
    }

    /**
     * 查询农作物信息列表
     * 
     * @param cropInfo 农作物信息
     * @return 农作物信息
     */
    @Override
    public List<CropInfo> selectCropInfoList(CropInfo cropInfo)
    {
        return cropInfoMapper.selectCropInfoList(cropInfo);
    }

    /**
     * 新增农作物信息
     * 
     * @param cropInfo 农作物信息
     * @return 结果
     */
    @Override
    public int insertCropInfo(CropInfo cropInfo)
    {
        return cropInfoMapper.insertCropInfo(cropInfo);
    }

    /**
     * 修改农作物信息
     * 
     * @param cropInfo 农作物信息
     * @return 结果
     */
    @Override
    public int updateCropInfo(CropInfo cropInfo)
    {
        return cropInfoMapper.updateCropInfo(cropInfo);
    }

    /**
     * 批量删除农作物信息
     * 
     * @param ids 需要删除的农作物信息主键
     * @return 结果
     */
    @Override
    public int deleteCropInfoByIds(Long[] ids)
    {
        return cropInfoMapper.deleteCropInfoByIds(ids);
    }

    /**
     * 删除农作物信息信息
     * 
     * @param id 农作物信息主键
     * @return 结果
     */
    @Override
    public int deleteCropInfoById(Long id)
    {
        return cropInfoMapper.deleteCropInfoById(id);
    }

    @Override
    public String importCropInfo(List<SysUser> userList, boolean updateSupport, String operName) {

        return "";
    }

    @Override
    public List<CropInfo> selectCropAll() {
        return cropInfoMapper.selectCropAll();
    }

    @Override
    public void insertPlanInfo(CropInfo planInfo) {
        cropInfoMapper.insertPlanInfo(planInfo);
    }

    @Override
    public List<CropInfo> selectCropInfoListAll(CropInfo cropInfo) {
        return cropInfoMapper.selectCropInfoListAll(cropInfo);
    }


}
