package com.ruoyi.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

//import com.ruoyi.file.service.MinioSysFileServiceImpl;
import com.alibaba.excel.EasyExcel;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.confing.EasyExcelConfig;
import com.ruoyi.confing.ExcelFillCellMergeStrategy;
import com.ruoyi.listener.DailyImportListener;
import com.ruoyi.service.IPlanInfoService;
import com.ruoyi.system.api.RemoteCorpService;
import com.ruoyi.system.api.converter.StringConverter;
import com.ruoyi.system.api.domain.CropInfo;
import com.ruoyi.system.api.domain.PlanInfo;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 农作物种植记录跟踪信息Controller
 *
 * @author ruoyi
 * @date 2024-02-28
 */
@RestController
@RequestMapping("/records")
public class PlanInfoController extends BaseController {
    @Autowired
    private IPlanInfoService planInfoService;
    @Autowired
    private RemoteCorpService remoteCorpService;
    //@Autowired
    //private MinioSysFileServiceImpl minioSysFileService;


    /**
     * 查询农作物种植记录跟踪信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PlanInfo planInfo) {
        startPage();
        List<PlanInfo> list = planInfoService.selectPlanInfoList(planInfo);
        return getDataTable(list);
    }

    ///**
    // * 导出农作物种植记录跟踪信息列表
    // */
    //@PostMapping("/export")
    //public void export(HttpServletResponse response, PlanInfo planInfo) {
    //    List<PlanInfo> list = planInfoService.selectPlanInfoList(planInfo);
    //    ExcelUtil<PlanInfo> util = new ExcelUtil<PlanInfo>(PlanInfo.class);
    //    util.exportExcel(response, list, "农作物种植记录跟踪信息数据");
    //}

    /**
     * 导出农作物种植记录跟踪信息列表
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<PlanInfo> list = planInfoService.selectPlanInfoListAll();
        //遍历循环
        for (PlanInfo planInfo : list) {
            //将状态字段替换
            if ("0".equals(planInfo.getDelFlag())) {
                planInfo.setDelFlag("未删除");
            } else {
                planInfo.setDelFlag("已删除");
            }
            //如果图片不为空 则下载到本地
            if (planInfo.getPic() != null && planInfo.getPic() != "") {
                //图片网络地址
                URI url = new URI(planInfo.getPic());

                URLConnection conn = url.toURL().openConnection();

                InputStream inputStream = conn.getInputStream();
                //替换图片名称
                String fileName =
                        com.ruoyi.common.core.utils.
                                uuid.UUID.randomUUID().toString().toString() +
                                planInfo.getPic().substring(planInfo.getPic().lastIndexOf("."));
                //图片在本地的存储位置
                String path = EasyExcelConfig.SYSPATH + fileName;
                FileOutputStream outputStream = new FileOutputStream(path);

                //写入到本地
                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();//关流
                inputStream.close();//关流

                planInfo.setPic(path);
            }
        }
        String fileName = "PlanInfo_" + new Date().getTime() + ".xls";
        response.setContentType(EasyExcelConfig.RESPONSE_TYPE);
        response.setCharacterEncoding(EasyExcelConfig.CHARSET);
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        //*  合并单元格的行索引
        int mergeRowIndex = 2;
        //*  合并单元格的列索引数组
        int[] mergeColumnIndex = {0};

        //使用EasyExcel将数据写入Excel表格中
        EasyExcel.write(response.getOutputStream(), PlanInfo.class)
                .registerWriteHandler(new ExcelFillCellMergeStrategy(mergeRowIndex, mergeColumnIndex))
                .sheet("种植数据").doWrite(list);
    }

    /**
     * 获取农作物种植记录跟踪信息详细信息
     */
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId) {
        return success(planInfoService.selectPlanInfoByPlanId(planId));
    }

    /**
     * 新增农作物种植记录跟踪信息
     */
    @PostMapping
    public AjaxResult add(@RequestBody PlanInfo planInfo) {
        //当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        planInfo.setUserId(loginUser.getUserid());
        planInfo.setRecordName(loginUser.getUsername());
        System.err.println("当前登录用户:" + loginUser);
        return toAjax(planInfoService.insertPlanInfo(planInfo));
    }

    /**
     * 修改农作物种植记录跟踪信息
     */
    @PutMapping
    public AjaxResult edit(@RequestBody PlanInfo planInfo) {
        return toAjax(planInfoService.updatePlanInfo(planInfo));
    }

    /**
     * 删除农作物种植记录跟踪信息
     */
    @DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds) {
        return toAjax(planInfoService.deletePlanInfoByPlanIds(planIds));
    }

    /**
     * 上传图片
     * @param file 图片文件
     * @return 图片文件名
     */
    //@PostMapping("uploadPic")
    //public String uploadPic(MultipartFile file) throws Exception {
    //    // 调用minioSysFileService服务上传图片
    //    String fileName = minioSysFileService.uploadPic(file);
    //    fileName = "http://localhost/dev-api/plant/records/downloadPic?fileName=" + fileName;
    //    return fileName;
    //}
    /**
     * 下载图片
     * @param fileName 图片文件名
     * @param response 响应对象
     * @throws Exception 异常
     */
    //@GetMapping("downloadPic")
    //public void downloadPic(String fileName, HttpServletResponse response) throws Exception {
    //    minioSysFileService.downloadPic(fileName, response);
    //}

    /**
     * 获取农作物列表 调用远程接口
     */
    @GetMapping("getCorpInfoList")
    public List<CropInfo> getCorpInfoList() {

        return remoteCorpService.getCorpInfoList();
    }

    /**
     * 根据农作物获取记录列表
     *
     * @param crop 作物信息
     * @return 记录列表
     */
    @GetMapping("pathListByCrop")
    public Object pathListByCrop(CropInfo crop) {
        // 调用记录信息服务的记录列表方法，传入作物信息
        List<PlanInfo> list = planInfoService.pathListByCrop(crop);
        // 返回路径列表
        return list;
    }

    /**
     * excel导入
     *
     * @param file
     * @param
     * @return
     * @throws Exception
     */

    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        // 这个转换是成全局的， 所有java为string,excel为string的都会用这个转换器。
        // 如果就想单个字段使用请使用@ExcelProperty 指定converter
        List<PlanInfo> list = EasyExcel.read(file.getInputStream())
                .registerConverter(new StringConverter())
                // 注册监听器，可以在这里校验字段
                .registerReadListener(new DailyImportListener())
                .head(PlanInfo.class)
                .sheet()
                .headRowNumber(2)
                .doReadSync();
        for (PlanInfo planInfo : list) {
            System.err.println();
            planInfoService.insertPlanInfo(planInfo);
        }
        return success("导入成功");
    }

}
