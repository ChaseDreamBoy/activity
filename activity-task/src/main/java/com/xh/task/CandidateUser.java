package com.xh.task;

import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.UUID;

// 候选人 -- ACT_RU_IDENTITYLINK
public class CandidateUser {

    public static void main(String[] args) {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();

        IdentityService identityService = engine.getIdentityService();
        String userId = UUID.randomUUID().toString();
        User user = identityService.newUser(userId);
        user.setFirstName("测试用户");
        identityService.saveUser(user);

        // 创建任务
        TaskService taskService = engine.getTaskService();
        // 任务是应该和流程结合在一起的
        // newTask 的任务是没有 流程的
        String taskId = UUID.randomUUID().toString();
        Task task = taskService.newTask(taskId);
        task.setName("测试任务");
        taskService.saveTask(task);

        // 设置 用户 的候选 用户组
        // ACT_RU_IDENTITYLINK 中就有数据了
        taskService.addCandidateUser(taskId, userId);

        // 该用户 有权限 处理 哪些用户  --
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(userId).list();
        System.out.println("该user id : " + userId + "有权限处理的任务有 :");
        for (Task taskInfo : taskList) {
            System.out.println("task name : " + taskInfo.getName());
        }

        engine.close();

        System.exit(0);
    }

}
