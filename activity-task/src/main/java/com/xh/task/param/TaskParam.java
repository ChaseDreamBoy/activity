package com.xh.task.param;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.UUID;

// 设置任务参数 -- ACT_RU_VARIABLE
public class TaskParam {

    public static void main(String[] args) {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();

        TaskService taskService = engine.getTaskService();
        // 任务是应该和流程结合在一起的
        // newTask 的任务是没有 流程的
        Task task = taskService.newTask(String.valueOf(UUID.randomUUID()));
        task.setName("测试任务3");
        taskService.saveTask(task);

        // 参数值 包含 8种  基本数据类型
        taskService.setVariable(task.getId(), "task_param_name1","task_param_value1");
        // 参数值为对象  会把对象序列化 保存到资源表ACT_GE_BYTEARRAY 中

        engine.close();

        System.exit(0);
    }

}
