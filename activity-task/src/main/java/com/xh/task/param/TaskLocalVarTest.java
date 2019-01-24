package com.xh.task.param;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

// 参数 作用域   local  本地有效
// 没有local  全局有效
public class TaskLocalVarTest {

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
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/task_local_var_test.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 启动流程  key是 xml中process 的 id  -- 到达第一个节点
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());

        // 查询第一个任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        // 请假天数为3天
        taskService.setVariableLocal(task.getId(), "days", 3);
        System.out.println("当前任务id : " + task.getId() + ", days 参数 : " + taskService.getVariableLocal(task.getId(), "days"));
        // 完成这个节点
        taskService.complete(task.getId());
        System.out.println("第一个任务 task 完成");

        // 查询第二个任务
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前任务id : " + task.getId() + ", days 参数 : " + taskService.getVariableLocal(task.getId(), "days"));


        engine.close();
        // 退出
        System.exit(0);
    }

}
