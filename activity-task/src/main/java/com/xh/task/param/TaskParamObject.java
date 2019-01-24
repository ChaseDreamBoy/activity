package com.xh.task.param;

import com.xh.task.entity.Person;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

// 设置任务参数 -- ACT_RU_VARIABLE
// 任务 参数为对象    参数值为对象  会把对象序列化 保存到资源表ACT_GE_BYTEARRAY 中
import java.util.UUID;

public class TaskParamObject {

    public static void main(String[] args) {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        TaskService taskService = engine.getTaskService();
        // 任务是应该和流程结合在一起的
        // newTask 的任务是没有 流程的
        Task task = taskService.newTask(UUID.randomUUID().toString());
        task.setName("测试任务5");
        taskService.saveTask(task);

        // task 参数为对象
        Person person = new Person(1, "person_name", 22, "describe...");
        taskService.setVariable(task.getId(), "person1", person);

        // 获取task参数
        Person personParam = taskService.getVariable(task.getId(), "person1", Person.class);
        System.out.println("task param is : \n" + personParam.toString());

        engine.close();
        System.exit(0);
    }

}
