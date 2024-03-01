package com.ruoyi.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.domain.Dictionary;
import com.ruoyi.system.api.domain.CropInfo;
import com.ruoyi.service.DictionaryService;
import com.ruoyi.service.ICropInfoService;
import com.ruoyi.util.DailyImportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



/**
 * 农作物信息Controller
 * 
 * @author ruoyi
 * @date 2024-02-28
 */
@RestController
@RequestMapping("/manage")
public class CropInfoController extends BaseController
{
    @Autowired
    private ICropInfoService cropInfoService;
    @Autowired
    DictionaryService dictionaryService;

    /**
    获取种植方式列表
     */
    @PostMapping("planList")
    public AjaxResult planList(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type","planMethod_info");
        List<Dictionary> list = dictionaryService.list(queryWrapper);
        return AjaxResult.success(list);
    }
    /**
    获取肥料类型列表
     */
    @PostMapping("fertList")
    public AjaxResult fertList(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type","fert_dic");
        List<Dictionary> list = dictionaryService.list(queryWrapper);
        return AjaxResult.success(list);
    }
 /**
    获取灌溉方式列表
     */
    @PostMapping("irrList")
    public AjaxResult irrList(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type","irrigation_dic");
        List<Dictionary> list = dictionaryService.list(queryWrapper);
        return AjaxResult.success(list);
    }

    /**
     * 查询农作物信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CropInfo cropInfo)
    {
        startPage();
        List<CropInfo> list = cropInfoService.selectCropInfoList(cropInfo);
        return getDataTable(list);
    }

    /**
     * 导出农作物信息列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, CropInfo cropInfo) throws IOException {

//        List<CropInfo> list = cropInfoService.selectCropInfoList(cropInfo);
//        System.err.println("导出的农作物信息对象:"+list);
//        ExcelUtil<CropInfo> util = new ExcelUtil<CropInfo>(CropInfo.class);
//        util.exportExcel(response, list, "农作物信息数据");
        List<CropInfo> list = cropInfoService.selectCropAll();
        //遍历循环

        response.setContentType(EasyExcelConfig.RESPONSE_TYPE);
        response.setCharacterEncoding(EasyExcelConfig.CHARSET);
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=test.xls");
        EasyExcel.write(response.getOutputStream(),CropInfo.class).sheet("农作物数据").doWrite(list);

    }
    /**
     * 查询农作物列表 不分页
     */
    @GetMapping ("/getCorpInfoList")
    public List<CropInfo> getCorpInfoList() {
        return cropInfoService.selectCropInfoListAll(new CropInfo());
    }
    /**
     * 导入农作物信息列表
     */
    @RequestMapping("lentInto")
    public AjaxResult getrntry(MultipartFile file) throws IOException {
        System.err.println("导入农作物信息列表进来了");
        List<CropInfo> list = EasyExcel.read(file.getInputStream())
                // 注册监听器，可以在这里校验字段
                .registerReadListener(new DailyImportListener())
                .head(CropInfo.class)
                .sheet()
                .headRowNumber(1)  //从第几行开始
                .doReadSync();
        System.err.println(list);
        for (CropInfo planInfo : list) {
            System.err.println(planInfo);
            this.add(planInfo);
        }

        return success("导入成功");

    }

    /**
     * 获取农作物信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cropInfoService.selectCropInfoById(id));
    }

    /**
     * 新增农作物信息
     */
    @PostMapping
    public AjaxResult add(@RequestBody CropInfo cropInfo)
    {
        /**
         * 给状态赋值为1
         */

        long l = IdUtil.getSnowflake().nextId();
        cropInfo.setStatus(1);
        System.err.println("新增列表:"+cropInfo);
        cropInfo.setCropNum(l);
        return toAjax(cropInfoService.insertCropInfo(cropInfo));
    }

    /**
     * 修改农作物信息
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CropInfo cropInfo)
    {
        return toAjax(cropInfoService.updateCropInfo(cropInfo));
    }

    /**
     * 删除农作物信息
     */
	@GetMapping("del")
    public AjaxResult remove( Long[] ids)
    {

        return toAjax(cropInfoService.deleteCropInfoByIds(ids));
    }
}
