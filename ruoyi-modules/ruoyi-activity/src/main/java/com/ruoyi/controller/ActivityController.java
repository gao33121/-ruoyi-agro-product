package com.ruoyi.controller;

import com.ruoyi.domain.SysExamine;
import com.ruoyi.domain.SysRegain;
import com.ruoyi.service.SysExamineService;
import com.ruoyi.service.SysRegainService;
import com.ruoyi.system.api.RemoteCorpService;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.RemoteActivityService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Description: TODO
 * @Author 卟哭！
 * @Date 2024/03/04  18:41
 * @Version 1.0
 */
@RequestMapping("activity")
@RestController
public class ActivityController {
    @Autowired
private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SysRegainService sysRegainService;
    @Autowired
    private SysExamineService sysExamineService;
    @Autowired
    private RemoteActivityService remoteActivityService;


    @PostMapping("deploy")
    public String deploy(@RequestBody SysRegain registain) {
        sysRegainService.save(registain);
    //    创建审批对象
        SysExamine sysExamine = new SysExamine();
    //    部署流程定义
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/RegainUser.bpmn20.xml")
                .addClasspathResource("process/RegainUser.png")
                .deploy();
        sysExamine.setRegainId(registain.getRegainId());
        sysExamine.setDecision(registain.getDescribes());
        sysExamine.setUserId(registain.getUserId());
        sysExamine.setDescribes(registain.getDescribes());
        HashMap<String, Object> map = new HashMap<>();
        map.put("createBy", registain.getCreateBy());
        map.put("decision", sysExamine.getDecision());
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();
        System.err.println(processDefinition.getId());
        ProcessInstance instance = runtimeService.startProcessInstanceById(processDefinition.getId(), sysExamine.getExamineId() + "", map);
        Task task = taskService.createTaskQuery()
                .processInstanceId(instance.getId())
                .singleResult();
        sysExamine.setTaskId(task.getId());
        sysExamine.setInstanceId(instance.getId());
        sysExamine.setExamineName(task.getName());
        sysExamine.setAssignee(task.getAssignee());
        sysExamineService.save(sysExamine);
        SysUser user = new SysUser();
        user.setUserId(registain.getUserId());
        user.setExamine("1");
        String s = remoteActivityService.updateUserExamine(user);
        return s;
    }




}
