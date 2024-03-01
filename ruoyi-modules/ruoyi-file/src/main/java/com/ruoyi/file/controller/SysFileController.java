package com.ruoyi.file.controller;

import com.ruoyi.file.config.MinioConfig;
import com.ruoyi.file.service.MinioSysFileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.file.FileUtils;
import com.ruoyi.file.service.ISysFileService;
import com.ruoyi.system.api.domain.SysFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件请求处理
 * 
 * @author ruoyi
 */
@RestController
public class SysFileController
{
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private ISysFileService sysFileService;
    @Autowired
    private MinioSysFileServiceImpl minioSysFileServiceImpl;


    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<SysFile> upload(MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return R.ok(sysFile);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }
    @PostMapping("uploadPic")
    public String uploadPic(MultipartFile file) throws Exception {
        //String DOWNLOAD_URL = "http://localhost/dev-api/plant/records/downloadPic";

        String fileName = minioSysFileServiceImpl.uploadPic(file);
        return  fileName;
    }
    @GetMapping("downloadPic")
    public void downloadPic(String fileName, HttpServletResponse response) throws Exception {
        minioSysFileServiceImpl.downloadPic(fileName, response);
    }

}