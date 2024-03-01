package com.ruoyi.file.service;

import java.io.InputStream;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import io.minio.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.nacos.common.utils.IoUtils;
import com.ruoyi.file.config.MinioConfig;
import com.ruoyi.file.utils.FileUploadUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * Minio 文件存储
 *
 * @author ruoyi
 */
@Service
public class MinioSysFileServiceImpl implements ISysFileService {
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;
    @Autowired
    MinioClient minioClient;


    /**
     * Minio文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = FileUploadUtils.extractFilename(file);
        InputStream inputStream = file.getInputStream();
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(inputStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        IoUtils.closeQuietly(inputStream);
        return minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + fileName;
    }

    /**
     * 上传图片到minio服务器
     *
     * @param file 要上传的文件
     * @return 上传后的文件名
     * @throws Exception
     */
    public String uploadPic(MultipartFile file) throws Exception {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 文件名添加UUID做唯一标识拼接
        fileName = UUID.randomUUID().toString() + "." + FileUtil.extName(fileName);
        // 检查minio服务器上的存储桶是否存在，如果不存在则创建
        boolean exist = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .build());
        if (!exist) {
            //创建存储桶
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
        }
        // 获取文件输入流
        InputStream inputStream = file.getInputStream();
        // 上传到minio服务器
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(inputStream, -1L, 10485760L)
                .build());
        return fileName;
    }

    /**
     * 下载图片
     *
     * @param fileName 图片文件名
     * @param response 响应对象
     * @throws Exception 异常
     */
    public void downloadPic(String fileName, HttpServletResponse response) throws Exception {
        try (
                // 获取一个输入流，用于读取存储在minio中的指定文件
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(minioConfig.getBucketName()) // 指定要获取文件的存储桶名称
                                .object(fileName) // 指定要获取的文件名
                                .build());

        ) {

            // 从流中读取数据
            byte[] bytes = IOUtils.toByteArray(inputStream);
            response.getOutputStream().write(bytes);
            // FileUtils.writeByteArrayToFile(new File("C:\\Users\\pc\\Desktop\\" + fileName), bytes);
            IOUtils.closeQuietly(inputStream);
        }
    }
}
