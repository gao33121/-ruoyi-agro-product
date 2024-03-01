package com.ruoyi.system.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.system.api.domain.CropInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description: TODO
 * @Author 卟哭！
 * @Date 2024/02/28  15:25
 * @Version 1.0
 */
@FeignClient(contextId = "remoteCorpService", value = ServiceNameConstants.PLANT_MANAGE_SERVICE)
public interface RemoteCorpService {
    @GetMapping ("/manage/getCorpInfoList")
     List<CropInfo> getCorpInfoList();
}
