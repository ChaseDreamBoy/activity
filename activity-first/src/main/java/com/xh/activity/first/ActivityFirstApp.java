package com.xh.activity.first;

import org.activiti.engine.*;
import org.activiti.engine.task.Task;

public class ActivityFirstApp {

    public static void main(String[] args) {
        // 创建流程引擎
//        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 配置文件 和bean 名称
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/first.bpmn").deploy();
        // 启动流程  key是 xml中process 的 id
        runtimeService.startProcessInstanceByKey("myProcess_1");
        // 查询第一个任务
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println("第一个任务完成前，当前任务名称：" + task.getName());
        // 完成第一个任务
        taskService.complete(task.getId());
        // 查询第二个任务
        task = taskService.createTaskQuery().singleResult();
        System.out.println("第二个任务完成前，当前任务名称：" + task.getName());
        // 完成第二个任务（流程结束）
        taskService.complete(task.getId());
        task = taskService.createTaskQuery().singleResult();
        System.out.println("流程结束后，查找任务：" + task);
        engine.close();
        // 退出
        System.exit(0);
    }

}
