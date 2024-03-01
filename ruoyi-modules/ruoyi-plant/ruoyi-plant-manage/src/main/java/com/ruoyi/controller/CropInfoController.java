package com.ruoyi.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.service.ICropInfoService;
import com.ruoyi.system.api.domain.CropInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 农作物信息Controller
 *
 * @author ruoyi
 * @date 2024-02-28
 */
@RestController
@RequestMapping("/manage")
public class CropInfoController extends BaseController {
    @Autowired
    private ICropInfoService cropInfoService;

    /**
     * 查询农作物信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CropInfo cropInfo) {
        startPage();
        List<CropInfo> list = cropInfoService.selectCropInfoList(cropInfo);
        return getDataTable(list);
    }

    /**
     * 导出农作物信息列表
     */
    //@Log(title = "农作物信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CropInfo cropInfo) {
        List<CropInfo> list = cropInfoService.selectCropInfoList(cropInfo);
        ExcelUtil<CropInfo> util = new ExcelUtil<CropInfo>(CropInfo.class);
        util.exportExcel(response, list, "农作物信息数据");
    }

    /**
     * 获取农作物信息详细信息
     */
    //@GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(cropInfoService.selectCropInfoById(id));
    }

    /**
     * 新增农作物信息
     */
    //@Log(title = "农作物信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CropInfo cropInfo) {
        return toAjax(cropInfoService.insertCropInfo(cropInfo));
    }

    /**
     * 修改农作物信息
     */
    //@Log(title = "农作物信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CropInfo cropInfo) {
        return toAjax(cropInfoService.updateCropInfo(cropInfo));
    }

    /**
     * 删除农作物信息
     */
    //@RequiresPermissions("system:info:remove")
    //@Log(title = "农作物信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cropInfoService.deleteCropInfoByIds(ids));
    }
    /**
     * 查询农作物列表 不分页
     */
    @GetMapping ("/getCorpInfoList")
    public List<CropInfo> getCorpInfoList() {
        return cropInfoService.selectCropInfoList(new CropInfo());
    }

}
