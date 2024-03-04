package com.ruoyi.system.api.model;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.system.api.domain.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: TODO
 * @Author 卟哭！
 * @Date 2024/03/03  15:23
 * @Version 1.0
 */
@FeignClient(contextId = "remoteActivityService", value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteActivityService {
    /**
     * 更改删除用户的申请恢复状态
     *
     * @param user
     * @return
     */
    @PostMapping  ("/user/updateUserExamine")
    public String updateUserExamine(@RequestBody SysUser user);
}
