package com.xh.task;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.UUID;

// 任务 代理人  -- ACT_RU_TASK 中 ASSIGNEE_ 字段
public class TaskClaim {

    public static void main(String[] args) {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();

        TaskService taskService = engine.getTaskService();
        // 任务是应该和流程结合在一起的
        // newTask 的任务是没有 流程的
        String taskId = String.valueOf(UUID.randomUUID());
        Task task = taskService.newTask(taskId);
        task.setName("测试任务3");
        taskService.saveTask(task);


        IdentityService identityService = engine.getIdentityService();
        String userId = UUID.randomUUID().toString();
        User user = identityService.newUser(userId);
        user.setFirstName("测试用户3");
        identityService.saveUser(user);

        taskService.setOwner(taskId, userId);
        // 设置代理 用户
        taskService.claim(taskId, userId);
        // 如果有代理人后  在指派其他人来代理  就会报错

        // 该用户 有权限 处理 哪些用户  --
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userId).list();
        System.out.println("该user id : " + userId + " 代理的任务有 :");
        for (Task taskInfo : taskList) {
            System.out.println("task name : " + taskInfo.getName());
        }

        engine.close();

        System.exit(0);
    }

}
